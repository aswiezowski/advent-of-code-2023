import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LensLibraryTest {


    @Test
    void getHASHValue() {
        // given
        var input = "HASH";

        // when
        var actual = LensLibrary.calculateHash(input);

        // then
        assertEquals(52, actual);
    }

    @Test
    void getSumOfHash() {
        // given
        var input = """
                    rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
                    
                    """;

        // when
        var texts = LensLibrary.parseInput(input);
        var actual = LensLibrary.getSumOfHash(texts);

        // then
        assertEquals(1320, actual);
    }

    @Test
    void sumOfBoxes() {
        // given
        var input = """
                    rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
                    
                    """;

        // when
        var texts = LensLibrary.parseInput(input);
        var actual = LensLibrary.getSumOfBoxes(texts);

        // then
        assertEquals(145, actual);
    }


}