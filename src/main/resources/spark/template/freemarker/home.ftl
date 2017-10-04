<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="/styles/main.css">
</head>
<body>
    <h1>${title}</h1>
    
    <div class="body">
    
      <h2>Application Stats</h2>
      <p>
        ${gameStatsMessage}
      </p>

      <h2>Current Stats</h2>
      <p>
        ${currentGameStatsMessage}
      </p>
      <p>Choose the game difficulty level:</p></br>
        <select id='level' onchange='changeValues()'>
          <option value="1" selected>Standard</option>
          <option value="2">Moderate</option>
          <option value="3">Difficult</option>
        </select>
        </br></br>
      <#if newSession>
        <p>
          <a id='playGame' href="/game?difficulty=1">Want to play a game?!?</a>
        </p>
      <#else>
        <#if youWon>
          <p>
            Congratulations!  You must have read my mind.
            <br/><br/>
            <a id='playGame' href="/game?difficulty=1">Do it again</a>
          </p>
        <#else>
          <p>
            Aww, too bad.  Better luck next time.
            <br/><br/>
            <a id='playGame' href="/game?difficulty=1">How about it?</a>
          </p>
        </#if>
      </#if>
        <script>
            function changeValues(){
                document.getElementById('playGame').href = "/game?difficulty=" + document.getElementById('level').value
            }
        </script>
    </div>
  </div>
</body>
</html>
