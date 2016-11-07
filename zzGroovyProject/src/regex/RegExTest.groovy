package regex

class RegExTest {

	/**
	* Short-hand character classes
	* \d (digit), \s (non-whitespace character),
	*
	* \w means "any word character" which usually means alphanumeric
	* (letters, numbers, regardless of case) plus underscore (_) i.e. [a-zA-Z0-9_]
	* \b (word boundary between word character and a non-word character)
	*
	* \w matches a word character. \b is a zero-width match that matches a position character
	* that has a word character on one side, and something that's not a word character on the other.
	*
	* \w matches a, b, c, d, e, and f in "abc def"
	* \b matches the (zero-width) position before a, after c, before d, and after f in "abc def"
	*/
	
	static void main(def args){
		
		println GroovySystem.version
		
		assert (/Count is \d/ == "Count is \\d")
		
		def name = "Ravi Asthana"
		assert (/$name/ == "Ravi Asthana")
		assert (/$name/ == "$name")
				
		/*
		 * Groovy Regular Expression Operators
		 * Groovy adds 3 new operators
		 * 
		 * "~" used before a string and it will cause the string to be compiled to a Pattern for later use
		 * 
		 * "=~" creates a matcher (i.e. returns a Matcher object) out of the String on the left hand 
		 * side and the Pattern on the right
		 * 
		 * "==~" returns a boolean that specifies if the full String matches the Pattern
		 * 
		 */
		// \b means word boundary, [A-Z] mean any capital letter, + means one or more
		def shoutedWord = ~/\b[A-Z]+\b/ //This matches any string of one or more capital letter with a
										//word boundary (non-word character on either side of it)
		
		def matcher = ("EUREKA" =~ shoutedWord)
		assert matcher.matches()
		
		def numberMatcher = "1234" =~ /\d+/
		assert numberMatcher.matches()
		
		assert "1234" ==~ /\d+/
		//assert "Foo2" ==~ /\d+/ //assertion fails here
		
		/*
		 * Enhancements to String class
		 * 
		 * In Groovy, the String class has been enhanced with a few “replace*” methods that allow you 
		 * to leverage regular expressions. These methods originally come from the Matcher class, but 
		 * attaching them directly to String puts them right at your finger-tips.
		 */
		assert "Green Eggs and Spam" == "Spam Spam".replaceFirst(/Spam/, "Green Eggs and")
		
		assert "The armor was colored silver" == "The armour was coloured silver".replaceAll("ou", "o")
		
		assert "firstName" == dashedToCamelCase("firstName")
		
		assert "612-###-####" == "612-555-1212".
			replaceAll(/(\d{3})-(\d{3})-(\d{4})/){fullMatch, areaCode, exchange, stationNumber ->
				assert fullMatch == "612-555-1212"
				assert areaCode == "612"
				assert exchange == "555"
				assert stationNumber == "1212"
				return "$areaCode-###-####"
		}
			
		/*
		 * Enhancements to Collections
		 * 
		 * regular expression aware iterator called "grep"	& "findAll"
		 */
		// regular expression aware iterator called "grep"	
		// regular expression says 0 or more characters (".*"), followed by the string "bar"
		// that is at the end of the string ("$")		
		assert ["foobar", "bazbar"] == ["foobar", "bazbar", "barquux"].grep(~/.*bar$/)
		// regular expression aware iterator called "findAll"
		assert ["foobar", "bazbar"] == ["foobar", "bazbar", "barquux"].findAll { it ==~ /.*bar$/}
		
		/*
		 * Working with Matchers
		 */
		
		def matcher2 = "foobazaarquux" =~ "o(b.*r)q" //start @ o and end @ q, o followed by b and
													  //then 0 or more characters to character 'r' ??
		assert ["obazaarq","bazaar"] == matcher2[0]
		assert "bazaar" == matcher2[0][1] //get the first grouping of the first map
		
		//above is little fragile as matcher2[0] will throw an error IF there was not actually a match
		//calling matches() doesn't help as matches only checks if the regular expression matches
		//the WHOLE string
		def matcher3 = "foobazaarquux" =~ "o(b.*r)q"
		println matcher3.matches()  // returns false!
		def matcher4 = "foobazaarquux" =~ ".*(b.*r).*"
		println matcher4.matches()  // returns true, ".*" matches 0 or more chars of any type
		
		//You can check getCount() to see how many matches there were for some safety:
		
		def m = "foobar" =~ /quux/
		if (m.getCount()) {
			// example won't get here as "quux" doesn't exist in "foobar", the count is 0
			println m[0]
		}
		
		//A groovier way to work with Matchers leverages collection iterators and the built in 
		//closures that Groovy provides to them
		def paragraph = """
			Lorem ipsum dolor 12:30 AM sit amet,
			consectetuer adipiscing 1:15 AM elit.
			Nunc rutrum diam sagittis nisi 9:22 PM.
		"""
	
		def HOUR = /10|11|12|[0-9]/
		def MINUTE = /[0-5][0-9]/
		def AM_PM = /AM|PM/
		def time = /($HOUR):($MINUTE) ($AM_PM)/
		
		//it[0] gives full match, it[1],it[2] and it[3] will give hour, min and AM/PM values
		assert ["12:30 AM", "1:15 AM", "9:22 PM"] == (paragraph =~ time).collect { it[0] }  
		
		assert ["12:30 AM", "1:15 AM"] == (paragraph =~ time).collect({it[0]}).grep(~/.*AM$/)
		
		// A limitation of the iterator-based methods is that they don’t give you access to the 
		// individual groups (hour, minute, am/pm), just the full matched string (“12:30 AM”). 
		// The "each" method is more powerful because as it iterates through, it passes the full 
		// match as well as each of the individual groups into the closure.
		("foo1 bar30 foo27 baz9 foo600" =~ /foo(\d+)/).each { match, digit -> println "+$digit" }
		
		// Another example (using the paragraph and time Matcher from above) showing how to pretty 
		// print all of the timestamps
		(paragraph =~ time).each {match, hour, minute, amPm ->
			println "$hour:$minute ${amPm == 'AM' ? 'this morning' : 'this evening' }"
		}
		
		/*
		 * Regular expressions are a powerful tool that Groovy makes as accessible as any other 
		 * top-tier scripting language. Using techniques to break more complicated regular 
		 * expressions into their component pieces can make them much more readable (as in the
		 * time example above).
		 * 
		 * If you’re doing any sort of string processing beyond a simple contains or split, 
		 * regular expressions in groovy can turn mountains of Java into a couple of lines of code.
		 */
	}
	
	/**
	 * Alternative version of replaceAll that takes a closure for the second parameter
	 * 
	 * Closure in always passed the full matched text of the regular expression as the first
	 * value, and then any matched groups(based on pattern) as subsequent values. 
	 * 
	 * This is specially useful in the situations where you want to manipulate the matched
	 * value, or groups within the match to dynamically determine the replacement text.
	 */
	def static dashedToCamelCase(orig) {
		// regular expression is a dash, followed by parenthesis that form a group where we hold 
		// the word's first character
		orig.replaceAll(/-(\w)/) { fullMatch, firstCharacter -> firstCharacter.toUpperCase() }
	}
}
