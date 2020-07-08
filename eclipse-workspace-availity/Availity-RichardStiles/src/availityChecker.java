
public class availityChecker {

	public static void main(String[] args) {
		String text = "(hello)()((what)(if))";
		availityChecker ac = new availityChecker();	
		boolean result = ac.parenthesesCheck(text);
		System.out.println("The parentheses check was successful (true/false) = " + result);
	}
	
	public boolean parenthesesCheck(String text) {
		int currentOpen = 0;
		int currentClose = 0;
		boolean success = true;
		if (text.length() > 0) {
			for(int a = 0; a < text.length(); a++) {
				if(text.substring(a, a+1).indexOf("(") == 0) {currentOpen++;}
				if(text.substring(a, a+1).indexOf(")") == 0) {currentClose++;}
				if(currentOpen < currentClose) {
					success = false;
					break;
				}
			}
			if(currentOpen != currentClose) {
				success = false;
			}
		}
		return success;
	}
}
