import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTests {

    @Test
    public void example1() {
        assertEquals(
            "000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000",
            Paintfuck.interpreter("*e*e*e*es*es*ws*ws*w*w*w*n*n*n*ssss*s*s*s*", 0, 6, 9),
            "Your interpreter should initialize all cells in the datagrid to 0"
        );
    }
    @Test
    public void example2() {
        assertEquals(
            "111100\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000\r\n000000",
            Paintfuck.interpreter("*e*e*e*es*es*ws*ws*w*w*w*n*n*n*ssss*s*s*s*", 7, 6, 9),
            "Your interpreter should adhere to the number of iterations specified"
        );
    }
    @Test
    public void example3() {
        assertEquals(
            "111100\r\n000010\r\n000001\r\n000010\r\n000100\r\n000000\r\n000000\r\n000000\r\n000000",
            Paintfuck.interpreter("*e*e*e*es*es*ws*ws*w*w*w*n*n*n*ssss*s*s*s*", 19, 6, 9),
            "Your interpreter should traverse the 2D datagrid correctly"
        );
    }
    @Test
    public void example4() {
        assertEquals(
            "111100\r\n100010\r\n100001\r\n100010\r\n111100\r\n100000\r\n100000\r\n100000\r\n100000",
            Paintfuck.interpreter("*e*e*e*es*es*ws*ws*w*w*w*n*n*n*ssss*s*s*s*", 42, 6, 9),
            "Your interpreter should traverse the 2D datagrid correctly for all of the \"n\", \"e\", \"s\" and \"w\" commands"
        );
    }
    @Test
    public void example5() {
        assertEquals(
            "111100\r\n100010\r\n100001\r\n100010\r\n111100\r\n100000\r\n100000\r\n100000\r\n100000",
            Paintfuck.interpreter("*e*e*e*es*es*ws*ws*w*w*w*n*n*n*ssss*s*s*s*", 100, 6, 9),
            "Your interpreter should terminate normally and return a representation of the final state of the 2D datagrid when all commands have been considered from left to right even if the number of iterations specified have not been fully performed"
        );
    }
    @Test
    public void example6() {
        assertEquals(
            "11000\r\n10000\r\n10000\r\n10000\r\n10000",
            Paintfuck.interpreter("*[s[e]*]", 23, 5, 5),
            "Your interpreter should terminate normally and return a representation of the final state of the 2D datagrid when all commands have been considered from left to right even if the number of iterations specified have not been fully performed"
        );
    }
}