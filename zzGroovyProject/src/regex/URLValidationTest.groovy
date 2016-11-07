package regex

class URLValidationTest {
	
	static void main(def args){
		/*
		String url = "example.com"
		String url = "sub.example.com"
		String url = "sub.domain.my-example.com"
		String url = "sub.domain.my-example.paris"
		String url = "example.com/?stuff=true"
		String url = "example.com:5000/?stuff=true"
		String url = "sub.domain.my-example.com/path/to/file/hello.html"
		String url = "hello.museum"
		String url = "http://railsgirls.com"
		String url = "abc@cde"
		*/
		String url = "www.crowntowers.com"
		if (!url.startsWith('http://')) url='http://'+url
		
		if(isURLValid(url)){
			println "VALID url"
		}else{
			println "INVALID url"
		}
	}
	
	static Boolean isURLValid(url){
		//url ==~/\b(https?|ftp|file):\/\/[-A-Za-z0-9+&@#\/%?=~_|!:,.;]*[-A-Za-z0-9+&@#\/%=~_|]/ //without .com/.org/.uk etc
		url ==~ /\b(https?|ftp|file):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+([*\.]{1}([a-z]+)))+([*\.]{1}([a-z]+))?(\/.*)?$/ //without port number
		//url ==~ /\b(https?|ftp|file):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]+(:[0-9]{1,5})?(\/.*)?$/ //with port number
	}
}
