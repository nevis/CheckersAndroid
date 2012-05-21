package ua.nevis.controller;

import ua.nevis.R;
import ua.nevis.model.Model;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CheckersAdapter extends BaseAdapter {
	//private LayoutInflater inflater;
	private int checkersField [] [] = new int[8][8];
    private int chip, enemyChip;
    private boolean turn = false;
    private int firstRow, firstColumn, killRow, killColumn;
    private boolean firstTurn = false;
    private Model model;
    private boolean noMulti = true;
    
    public CheckersAdapter(Model model) {
       this.model = model;
    }
	@Override
	public int getCount() {
		return 64;
	}
	@Override
	public Object getItem(int index) {
		return checkersField[(int)index/8][(int)index%8];
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = new ImageView(Model.getInstance().getClientCommand().getContext());
		}
		int value = checkersField[(int)position/8][(int)position%8];
		if (value == 1) {
			((ImageView) view).setImageResource(R.drawable.white11);
        } else if (value == 2) {
        	((ImageView) view).setImageResource(R.drawable.black11);
        } else if (value == 11) {
        	((ImageView) view).setImageResource(R.drawable.white21);
        } else if (value == 21) {
        	((ImageView) view).setImageResource(R.drawable.black21);
        } else if (value == 0) {
        	((ImageView) view).setImageResource(R.drawable.blackcell1);
        } else if (value == -1) {
        	((ImageView) view).setImageResource(R.drawable.whitecell1);
        }
		return view;
	}
    public void refreshCheckersBoard() {
    	for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {
                    setOnChip(i, j);
                } else if ((i + j) % 2 == 0) {
                    checkersField [i][j] = -1;
                }
            }
        }
    }
    private void setOnChip(int i, int j) {
        if (chip == 1) {
            if ((i * 8 + j) < 24) checkersField [i][j] = 2;
            else if ((i * 8 + j) > 39) checkersField [i][j] = 1;
            else checkersField [i][j] = 0;
        } else if (chip == 2){
            if ((i * 8 + j) < 24) checkersField [i][j] = 1;
            else if ((i * 8 + j) > 39) checkersField [i][j] = 2;
            else checkersField [i][j] = 0;
        }
    }
    public int getCheckersBoardValueAt(int i, int j) {
        return checkersField[i][j];
    }
    public void setCheckersBoardValue(int x, int y, int value) {
        checkersField[x][y] = value;
    }
    public void setChip(int chip) {
        this.chip = chip;
        if (chip == 1) enemyChip = 2;
        else enemyChip = 1;
    }
    public int getChip() {
        return chip;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public boolean getTurn() {
        return turn;
    }
    
    public void clientTurn(int row, int column) {
        if (!firstTurn) {
            firstTurn = isSelected(row, column);
        } else {
            if (noMulti && (!checkToKillAll()) && (turn(row, column))) {
                setChips(row, column);
                model.getClient().sendMessage("@turn;" + firstRow + ";" + firstColumn + ";"
                        + row + ";" + column + ";" + checkersField[row][column] + ";");
                firstTurn = false;
                notifyDataSetChanged();
                turn = false;
            } else if (killTurn(row, column)){
                if (checkersField[firstRow][firstColumn] == chip) {
                    row = firstRow + 2 * killRow;
                    column = firstColumn + 2 * killColumn;
                }
                setChips(row, column);
                checkersField[firstRow + killRow][firstColumn + killColumn] = 0;
                if (checkToKill(row, column)) {
                    model.getClient().sendMessage("@kill;" + firstRow + ";" + firstColumn + ";"
                            + row + ";" + column + ";" + killRow + ";" + killColumn + ";" + "noend;"
                            + checkersField[row][column] + ";");
                    noMulti = false;
                    firstRow = row;
                    firstColumn = column;
                    notifyDataSetChanged();
                    turn = true;
                } else {
                    model.getClient().sendMessage("@kill;" + firstRow + ";" + firstColumn + ";"
                            + row + ";" + column + ";" + killRow + ";" + killColumn + ";" + "end;"
                            + checkersField[row][column] + ";");
                    noMulti = true;
                    firstTurn = false;
                    notifyDataSetChanged();
                    turn = false;
                }
            }  else if (noMulti) {
                if (!isSelected(row, column)) firstTurn = false;
            }
        }
    }
    private void setChips(int row, int column) {
        if ((row == 0) && (checkersField[firstRow][firstColumn] == chip)) checkersField[row][column] = chip*10 + 1;
        else if (checkersField[firstRow][firstColumn] == chip) checkersField[row][column] = chip;
        else if (checkersField[firstRow][firstColumn] == chip*10 + 1) checkersField[row][column] = chip*10 + 1;
        checkersField[firstRow][firstColumn] = 0;
    }
    private boolean isSelected(int row, int column) {
        if ((checkersField[row][column] == chip) || (checkersField[row][column] == chip*10 + 1)) {
            firstRow = row;
            firstColumn = column;
            return true;
        } else
            return false;
    }
    private boolean turn(int row, int column) {
        boolean turn = false;
        if (checkersField[firstRow][firstColumn] == chip) {
              turn = simpleTurn(row, column);
        } else if ((checkersField[firstRow][firstColumn] == chip*10 + 1) && (checkersField[row][column] == 0) && 
                (Math.abs(row - firstRow) == Math.abs(column - firstColumn))){
                turn = multiTurn(row, column);
            }
        return turn;
    }
    private boolean simpleTurn(int row, int column) {
        if ((row - firstRow == -1) && (column - firstColumn == -1) && (checkersField[row][column] == 0))
            return true;
        else if ((row - firstRow == -1) && (column - firstColumn == 1) && (checkersField[row][column] == 0))
            return true;
        else
            return false; 
    }
    private boolean multiTurn(int row, int column) {
        boolean turn = false;
        if (row - firstRow < 0 && column - firstColumn < 0) {
            if (getLeftUpChipNumber(firstRow, firstColumn, Math.abs(row - firstRow)) == 0) turn = true;
        } else if (row - firstRow > 0 && column - firstColumn < 0) {
            if (getLeftDownChipNumber(firstRow, firstColumn, Math.abs(row - firstRow)) == 0) turn = true;
        } else if (row - firstRow < 0 && column - firstColumn > 0) {
            if (getRightUpChipNumber(firstRow, firstColumn, Math.abs(row - firstRow)) == 0) turn = true;
        } else if (row - firstRow > 0 && column - firstColumn > 0) {
            if (getRightDownChipNumber(firstRow, firstColumn, Math.abs(row - firstRow)) == 0) turn = true;
        }
        return turn;
    }
    private boolean killTurn(int row, int column) {
        boolean kill = false;
        int clientLoc = Math.abs(row - firstRow);
        if (checkersField[firstRow][firstColumn] == chip) clientLoc = 2;
        if (clientLoc == Math.abs(column - firstColumn)) {
            for (int i = 1; i < clientLoc; i++) {
                if (isLeftUpKill(firstRow, firstColumn, i, clientLoc)) {
                    killRow = -i;
                    killColumn = -i;
                    kill = true;
                    break;
                } else if (isLeftDownKill(firstRow, firstColumn, i, clientLoc)) {
                    killRow = i;
                    killColumn = -i;
                    kill = true;
                    break;
                } else if (isRightUpKill(firstRow, firstColumn, i, clientLoc)) {
                    killRow = -i;
                    killColumn = i;
                    kill = true;
                    break;
                } else if (isRightDownKill(firstRow, firstColumn, i, clientLoc)) {
                    killRow = i;
                    killColumn = i;
                    kill = true;
                    break;
                }
            }
        }
        return kill;
    }
    private boolean checkToKillAll() {
        boolean kill = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkToKill(i, j)) {
                    kill = true;
                    break;
                }
            }
            if (kill) break;
        }
        return kill;
    }
    private boolean checkToKill(int row, int column) {
        boolean kill = false;
        if (checkersField[row][column] == chip) { //simple chip
            if (isHaveToKill(row, column, 1, 2)) {
                kill = true;
            }
        } else if (checkersField[row][column] == chip*10 + 1) { //super chip
            for (int ii = 1; ii < 8; ii++) {
                if (isHaveToKill(row, column, ii, ii+1)) {
                    kill = true;
                }
            }
        }
        return kill;
    }
    private boolean isHaveToKill(int row, int column, int enemyLoc, int clientLoc) {
        if (isLeftUpKill(row, column, enemyLoc, clientLoc)) {
            return true;
        } else if (isLeftDownKill(row, column, enemyLoc, clientLoc)) {
            return true;
        } else if (isRightUpKill(row, column, enemyLoc, clientLoc)) {
            return true;
        } else if (isRightDownKill(row, column, enemyLoc, clientLoc)) {
            return true;
        } else
            return false;
    }
    private boolean isLeftUpKill(int row, int column, int enemyLoc, int clientLoc) {
        int kol = 0;
        if ((row - clientLoc >= 0) && (column - clientLoc >= 0) &&
                (checkersField[row - clientLoc][column - clientLoc] == 0) &&
                ((checkersField[row - enemyLoc][column - enemyLoc] == enemyChip) ||
                        (checkersField[row - enemyLoc][column - enemyLoc] == enemyChip*10 + 1))) {
            kol = getLeftUpChipNumber(row, column, clientLoc);
        }
        if (kol == 1) return true;
        else return false;
    }
    private int getLeftUpChipNumber(int row, int column, int clientLoc) {
        int number = 0;
        for (int i = 1; i < clientLoc; i++) {
            if (checkersField[row-i][column-i] == enemyChip ||
                    checkersField[row-i][column-i] == enemyChip*10+1 ||
                    checkersField[row-i][column-i] == chip ||
                    checkersField[row-i][column-i] == chip*10+1) number += 1;
        }
        return number;
    }
    private boolean isLeftDownKill(int row, int column, int enemyLoc, int clientLoc) {
        int kol = 0;
        if ((row + clientLoc <= 7) && (column - clientLoc >= 0) &&
                (checkersField[row + clientLoc][column - clientLoc] == 0) &&
                ((checkersField[row + enemyLoc][column - enemyLoc] == enemyChip) ||
                        (checkersField[row + enemyLoc][column - enemyLoc] == enemyChip*10 + 1))) {
                kol = getLeftDownChipNumber(row, column, clientLoc);
        }
        if (kol == 1) return true;
        else return false;
    }
    private int getLeftDownChipNumber(int row, int column, int clientLoc) {
        int number = 0;
        for (int i = 1; i < clientLoc; i++) {
            if (checkersField[row+i][column-i] == enemyChip ||
                    checkersField[row+i][column-i] == enemyChip*10+1 ||
                    checkersField[row+i][column-i] == chip ||
                    checkersField[row+i][column-i] == chip*10+1) number += 1;
        }
        return number;
    }
    private boolean isRightUpKill(int row, int column, int enemyLoc, int clientLoc) {
        int kol = 0;
        if ((row - clientLoc >= 0) && (column + clientLoc <= 7) &&
                (checkersField[row - clientLoc][column + clientLoc] == 0) &&
                ((checkersField[row - enemyLoc][column + enemyLoc] == enemyChip) ||
                        (checkersField[row - enemyLoc][column + enemyLoc] == enemyChip*10 + 1))) {
            kol = getRightUpChipNumber(row, column, clientLoc);
        }
        if (kol == 1) return true;
        else return false;
    }
    private int getRightUpChipNumber(int row, int column, int clientLoc) {
        int number = 0;
        for (int i = 1; i < clientLoc; i++) {
            if (checkersField[row-i][column+i] == enemyChip ||
                    checkersField[row-i][column+i] == enemyChip*10+1 ||
                    checkersField[row-i][column+i] == chip ||
                    checkersField[row-i][column+i] == chip*10+1) number += 1;
        }
        return number;
    }
    private boolean isRightDownKill(int row, int column, int enemyLoc, int clientLoc) {
        int kol = 0;
        if ((row + clientLoc <= 7) && (column + clientLoc <= 7) &&
                (checkersField[row + clientLoc][column + clientLoc] == 0) &&
                ((checkersField[row + enemyLoc][column + enemyLoc] == enemyChip) ||
                        (checkersField[row + enemyLoc][column + enemyLoc] == enemyChip*10 + 1))) {
        kol = getRightDownChipNumber(row, column, clientLoc);
        }
        if (kol == 1) return true;
        else return false;
    }
    private int getRightDownChipNumber(int row, int column, int clientLoc) {
        int number = 0;
        for (int i = 1; i < clientLoc; i++) {
            if (checkersField[row+i][column+i] == enemyChip ||
                    checkersField[row+i][column+i] == enemyChip*10+1 ||
                    checkersField[row+i][column+i] == chip ||
                    checkersField[row+i][column+i] == chip*10+1 ) number += 1;
        }
        return number;
    }
}
