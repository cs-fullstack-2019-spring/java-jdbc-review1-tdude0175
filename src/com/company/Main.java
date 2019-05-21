package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/halfday_project";
    private static final String user = "student";
    private static final String password = "C0d3Cr3w";

    public static Connection connect() throws SQLException
    {
        return DriverManager.getConnection(URL,user,password);
    }

    public static void matchResults(int matchIDNumber, String teamOne , String teamTwo)
    {
        int team1Score = 0;
        int team2Score = 0;
        String SQL ="select matchid, teamid  from goal where matchid = ?";
        try
        {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL) ;
            pstmt.setInt(1,matchIDNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
//                System.out.print(rs.getString(1) + "\t");
//                System.out.println(rs.getString(2));
                if(rs.getString(2).equalsIgnoreCase(teamOne))
                {
                    team1Score++;
                }
                else
                    {
                        team2Score++;
                    }
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        System.out.print(teamOne+" ");
        System.out.print(team1Score+"\t");
        System.out.print(teamTwo+" ");
        System.out.println(team2Score);

        if(team1Score > team2Score)
        {
            System.out.println(teamOne + " Won \n");
        }
        else if(team2Score > team1Score)
        {
            System.out.println(teamTwo + " Won \n");
        }
        else
            {
                System.out.println("Ending in a draw \n");
            }
    }

    public static void SearchMatches()
    {

        String SQL = "SELECT * from game";

        try
        {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next())
            {
//                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2));
                System.out.println(" at the " +rs.getString(3)+"\t");
                System.out.print(rs.getString(4)+" went against ");
                System.out.print(rs.getString(5)+" with the points being ");
//                System.out.print(rs.getString(7)+"\t");
//                System.out.print(rs.getString(8)+"\t");
//                System.out.println(rs.getString(9)+"\n");
                matchResults(rs.getInt(1),rs.getString(4),rs.getString(5));
            }
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public static void addGoal()
    {
        Scanner answerToQuestions = new Scanner(System.in);

        System.out.println("What is the match id?");
        String matchID = answerToQuestions.next();
        System.out.println("What was the scoring team id?");
        String teamID = answerToQuestions.next();
        System.out.println("Who made the point?");
        String playerWhoScoredFirstName = answerToQuestions.next();
        String playerWhoScoredLastName = answerToQuestions.next();
        System.out.println("When was the goal scored?(in minutes)");
        String gameTime = answerToQuestions.next();

        String player = playerWhoScoredFirstName +" "+playerWhoScoredLastName;
        System.out.println(matchID+ " " + teamID + " " + " " +player+ " " +gameTime);
        System.out.println("Is this info Correct?");
        String isDataCorrect = answerToQuestions.next();
        if(isDataCorrect.equalsIgnoreCase("yes"))
        {
            saveGoalMade(Integer.parseInt(matchID),teamID,player,Integer.parseInt(gameTime));
        }
    }

    public static void saveGoalMade(int matchID, String teamID , String scoringPlayer , int timeGoalWasMade)
    {
        String SQL = "insert into goal(matchid, teamid, player, gtime) VALUES(?,?,?,?)";

        try
        {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1,matchID);
            pstmt.setString(2,teamID);
            pstmt.setString(3,scoringPlayer);
            pstmt.setInt(4,timeGoalWasMade);
            pstmt.executeUpdate();
            System.out.println("Goal Recorded");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public static void main(String[] args) {
        boolean exit = false;
        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome to the Official CodeSchool FootBall managing app App");
        do
            {

                System.out.println("What would you like to do?");
                System.out.println("1. List all match results");
                System.out.println("2. Add a goal to the database?");
                System.out.println("3. Exit application");
                String input = userInput.next();
                if(input.equalsIgnoreCase("1"))
                {
                    System.out.println("Here are the matches"+"\n");
                    SearchMatches();
                }
                else if(input.equalsIgnoreCase("2"))
                {
                    System.out.println("Alright, please answer these questions."+"\n");
                    addGoal();
                }
                else if(input.equalsIgnoreCase("3"))
                {
                    System.out.println("Have a good day");
                    exit = true;
                }
                else
                    {
                        System.out.println("Not valid command. Please enter a different command"+"\n");
                    }
            }
        while (!exit);
    }
}
//jdbc:postgresql://localhost:5432/halfday_project