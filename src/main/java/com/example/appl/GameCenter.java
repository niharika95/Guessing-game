package com.example.appl;

import java.util.Objects;
import java.util.logging.Logger;

import spark.Session;

import com.example.model.GuessGame;


/**
 * The object to coordinate the state of the Web Application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GameCenter {
  private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

  //
  // Constants
  //

  final static String NO_GAMES_MESSAGE = "No games has been played so far.";
  final static String ONE_GAME_MESSAGE = "One game has been played so far and the average win is %3.2f percent.";
  final static String GAMES_PLAYED_FORMAT = "There have been %d games played and the average win is %3.2f percent.";

  final static String LOCAL_NO_GAMES_MESSAGE = "No game stats yet.";
  final static String LOCAL_NO_WINS_GAME_MESSAGE = "You have not won a game, yet. But I *feel* your luck changing.";
  final static String LOCAL_GAMES_PLAYED_FORMAT = "You have won an average of %3.2f percent of this session's %d games.";

  /**
   * The user session attribute name that points to a game object.
   */
  public final static String GAME_ID = "game";

  //
  // Attributes
  //

  private int totalGames = 0;
  private int localGamesWon = 0;
  private int localGames = 0;
  private int globalGamesWon = 0;

  public int getGlobalGamesWon() {
    return globalGamesWon;
  }

  public void setGlobalGamesWon(int globalGamesWon) {
    this.globalGamesWon = globalGamesWon;
  }

  public int getLocalGamesWon() {
    return localGamesWon;
  }

  public void setLocalGamesWon(int localGamesWon) {
    this.localGamesWon = localGamesWon;
  }

  public int getLocalGames() {
    return localGames;
  }

  public void setLocalGames(int localGames) {
    this.localGames = localGames;
  }

  //
  // Public methods
  //

  /**
   * Get the {@linkplain GuessGame game} for the current user
   * (identified by a browser session).
   *
   * @param session
   *   The HTTP session
   *
   * @return
   *   A existing or new {@link GuessGame}
   */
  public GuessGame get(final Session session) {
    // validation
    Objects.requireNonNull(session, "session must not be null");
    //
    GuessGame game = session.attribute(GAME_ID);
    if (game == null) {
      // create new game
      game = new GuessGame();
      session.attribute(GAME_ID, game);
      LOG.fine("New game created: " + game);
    }
    return game;
  }

  /**
   * End the user's current {@linkplain GuessGame game}
   * and remove it from the session.
   *
   * @param session
   *   The HTTP session
   */
  public void end(Session session) {
    // validation
    Objects.requireNonNull(session, "session must not be null");
    // remove the game from the user's session
    session.removeAttribute(GAME_ID);
    // do some application-wide book-keeping
    synchronized (this) {  // protect the critical code
      totalGames++;
      localGames++;
    }
  }

  /**
   * Get a user message about the statistics for the whole site.
   *
   * @return
   *   The message to the user about global game statistics.
   */
  public synchronized String getGameStatsMessage() {
    double avg;
    if (totalGames > 1) {
      avg = ((double) globalGamesWon /(double)totalGames)*100;
      return String.format(GAMES_PLAYED_FORMAT, totalGames, avg);
    } else if (totalGames == 1) {
      avg = ((double) globalGamesWon /(double)totalGames)*100;
      return String.format(ONE_GAME_MESSAGE, avg);
    } else {
      return NO_GAMES_MESSAGE;
    }
  }

  public synchronized String getCurrentGameStatsMessage() {
    double avg;
    if (localGames > 1) {
      avg = ((double) localGamesWon /(double)localGames)*100;
      if (localGamesWon == 0){
        return LOCAL_NO_WINS_GAME_MESSAGE;
      }
      return String.format(LOCAL_GAMES_PLAYED_FORMAT, localGames, avg);
    }else {
      return LOCAL_NO_GAMES_MESSAGE;
    }
  }
}