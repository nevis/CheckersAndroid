package ua.nevis.model;

public class Commands {
	public enum Command {
        Connected("@connected"),
        PlayerList("@playerlist"),
        AddPlayer("@addplayer"),
        RemovePlayer("@removeplayer"),
        Invite("@invite"),
        NoFindPlayer("@nofind"),
        AcceptGame("@accept"),
        RejectInvite("@rejectinvite"),
        Turn("@turn"),
        Kill("@kill"),
        Lose("@lose"),
        Win("@win"),
        EnemyOff("@enemyOff");

        private String command;
        Command(String command) {
            this.command = command;
        }
        private String getCommand() {
            return command;
        }
    }
    public Command getClientCommand(String s) {
        if (s.equals(Command.Connected.getCommand())) return Command.Connected;
        else if (s.equals(Command.PlayerList.getCommand())) return Command.PlayerList;
        else if (s.equals(Command.AddPlayer.getCommand())) return Command.AddPlayer;
        else if (s.equals(Command.RemovePlayer.getCommand())) return Command.RemovePlayer;
        else if (s.equals(Command.Invite.getCommand())) return Command.Invite;
        else if (s.equals(Command.NoFindPlayer.getCommand())) return Command.NoFindPlayer;
        else if (s.equals(Command.AcceptGame.getCommand())) return Command.AcceptGame;
        else if (s.equals(Command.RejectInvite.getCommand())) return Command.RejectInvite;
        else if (s.equals(Command.Turn.getCommand())) return Command.Turn;
        else if (s.equals(Command.Kill.getCommand())) return Command.Kill;
        else if (s.equals(Command.Lose.getCommand())) return Command.Lose;
        else if (s.equals(Command.Win.getCommand())) return Command.Win;
        else if (s.equals(Command.EnemyOff.getCommand())) return Command.EnemyOff;
        else throw new AssertionError("Unknown command: " + s);
    }
}
