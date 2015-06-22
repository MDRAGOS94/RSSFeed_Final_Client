package JUint;

import static org.junit.Assert.*;

import org.junit.Test;

import ro.traistaruandszasz.rssfeed.validate.Validator;

public class ValidatorTest {

    private static final String correctInput = "test";
    private static final String incorrectLenghtInput = "";
    private static final String incorrectInput = "%^test12";
    
    
    @Test
    public void inputTrue() {
	boolean result = Validator.isAcceptedStringInput(correctInput);
	assertTrue(result);
    }
    @Test
    public void inputFalse() {
	boolean result = Validator.isAcceptedStringInput(incorrectInput);
	assertFalse(result);
    }
    
    @Test
    public void inputTextAreaTrue() {
	boolean result = Validator.isAcceptedTextAreaInput(correctInput);
	assertTrue(result);
    }
    @Test
    public void inputTextAreaFalse() {
	boolean result = Validator.isAcceptedTextAreaInput(incorrectLenghtInput);
	assertFalse(result);
    }

}
