import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NodeTest {
    private BaoNode <String> first, second;

    @BeforeEach
    void setUp() {
        first=new BaoNode<>("First");
        second=new BaoNode<>("Second");
    }

    @AfterEach
    void tearDown() {
        first=null;
        second=null;
    }

    @Test
    void constructorTest() {
        first=new BaoNode<>(null); //Null content
        assertNull(first.getContent());
        assertNull(first.getNext()); //Initial state
        assertNull(first.getPrev());
    }

    @Test
    void getSetTest() {
        first.setNext(second);
        second.setPrev(first);

        assertEquals(second, first.getNext());
        assertEquals(first, second.getPrev());
    }
}
