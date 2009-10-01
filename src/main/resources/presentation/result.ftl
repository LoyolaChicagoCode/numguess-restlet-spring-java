<html>
<head>
<title>Number Guessing Game: Result</title>
</head>
<body>
<#if comparison == 0>
<p>Yahoo!</p>
<#else> 
<#if comparison < 0>
<p>Too low.</p>
<#else>
<p>Too high.</p>
</#if>
<form method="post"><input type="text" name="guess" /> <input
	type="submit" value="guess" /></form>
</#if>
<p>You have made ${numGuesses} guesses so far.</p>
<#if newBestScore>
<p>That is a new best score!</p>
</#if>
</body>
</html>
