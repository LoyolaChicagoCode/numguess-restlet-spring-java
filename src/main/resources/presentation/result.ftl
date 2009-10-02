<!DOCTYPE
 html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
 "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
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
			<form method="post">
				<input type="text" name="guess" />
				<input type="submit" value="guess" />
			</form>
		</#if>
		<p>You have made ${numGuesses} guesses so far.</p>
		<#if newBestScore>
			<p>That is a new best score!</p>
		</#if>
		<form method="post" action=".">
			<div>
				<input type="submit" value="play again" />
			</div>
		</form>
	</body>
</html>
