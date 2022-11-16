import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarpleTest {

    @Test
    public void sampleTest1() {
        String[] clues = new String[]{
            "MRT", "ABH", "LKO", "OKP", "JIM", "OPE", "GDO", "RAQ", "J^A", "M^P", "A<Q", "D<K", "OQO"
        };

        assertEquals("EDBACGHIJFLMKONSPRTQ", MarpleSolver.solve(clues));
    }


    @Test
    public void sampleTest2() {
        String[] clues = new String[]{
            "KLA",
            "JAB",
            "FJH",
            "CMF",
            "AQT",
            "GBN",
            "MAF",
            "Q^B",
            "J^E",
            "R^A",
            "M<R",
            "E<N",
            "N<F",
            "AMA",
            "MCM",
            "EPE"
        };

        assertEquals("CEABDHJIFGOMNLKPSRQT", MarpleSolver.solve(clues));
    }

    @Test
    public void sampleTest3() {
        String[] clues = new String[]{
            "PBJ", "KDO", "DHG", "AOR", "INM", "EMB", "GTD", "O^T", "P<Q", "T<P", "A<L", "P<F", "RIR", "IDI"
        };

        assertEquals("CDBAEIJHGFKNOMLSRTPQ", MarpleSolver.solve(clues));
    }

    @Test
    public void sampleTest4() {
        String[] clues = new String[]{
            "EMJ", "DJO", "AMN", "ADC", "CIL", "END", "GQS", "SAB", "Q<B", "RPR", "SAS", "FNF", "NPN", "SCS"
        };

        assertEquals("ECDABFHIJGKNMLOPRSQT", MarpleSolver.solve(clues));
    }

    @Test
    public void sampleTest5() {
        String[] clues = new String[]{
            "LCI",
            "CQH",
            "NOF",
            "AEC",
            "APG",
            "NGL",
            "EQB",
            "F^P",
            "M^S",
            "E<J",
            "B<F",
            "T<P",
            "F<A",
            "P<K",
            "S<Q",
            "LCL"
        };

        assertEquals("BDCEAHIGFJMNOLKSQTPR", MarpleSolver.solve(clues));
    }
}