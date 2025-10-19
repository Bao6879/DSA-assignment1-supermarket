import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

///ADDING, SUBLIST GENERATES DEEP COPIES, SO CHECK CONTENT EQUALS INSTEAD
public class LinkedListTest {
    private BaoList <String> list;
    private BaoNode <String> first, second, third;

    @BeforeEach
    void setUp() {
        list=new BaoList<>();
        first=new BaoNode<>("First");
        second=new BaoNode<>("Second");
        third=new BaoNode<>("Third");
    }

    @AfterEach
    void tearDown() {
        list.clear();
        first=null;
        second=null;
        third=null;
    }

    @Test
    void constructorTest() {
        assertNull(list.getNode(0));
        assertEquals(0, list.getSize());
    }

    @Test
    void addAndGetTests()
    {
        assertNull(list.getNode(0)); //Empty list
        list.addNode(first);
        assertEquals("First", list.getNode(0).getContent()); //First
        assertEquals(1, list.getSize());

        list.addNode(second);
        assertEquals("Second", list.getNode(1).getContent()); //Already has something
        assertEquals(2, list.getSize());

        list.addNode((BaoNode<String>) null);
        assertNull(list.getNode(2)); //Doesn't add null
        assertEquals(2, list.getSize());

        list.addNode(third);
        assertEquals("Third", list.getNode(2).getContent());
        assertEquals(3, list.getSize());

        assertEquals(list.getNode(0).getNext().getContent(), second.getContent()); //Checks order
        assertEquals(list.getNode(1).getPrev().getContent(), first.getContent());
        assertEquals(list.getNode(1).getNext().getContent(), third.getContent());
        assertEquals(list.getNode(2).getPrev().getContent(), second.getContent());

        assertNull(list.getNode(3)); //Non-existent elements
        assertNull(list.getNode(-1));
    }

    @Test
    void removeTests()
    {
        list.removeNode(first); //Empty list
        list.addNode(first);
        list.addNode(second);
        list.addNode(third);

        list.removeNode(second); //Middle node
        assertEquals(2, list.getSize());
        assertEquals(first.getContent(), list.searchNode(third).getPrev().getContent());
        assertEquals(third.getContent(), list.searchNode(first).getNext().getContent());
        assertEquals(third.getContent(), list.getNode(1).getContent());

        list.removeNode(first); //Head node
        assertEquals(1, list.getSize());
        assertEquals(third.getContent(), list.getNode(0).getContent());

        list.removeNode(second); //Non-existent node

        list.removeNode(third); //Head and last node
        assertEquals(0, list.getSize());
        assertNull(list.getNode(0));
    }

    @Test
    void searchTests()
    {
        assertNull(list.searchNode(first)); //Empty list

        list.addNode(first);
        list.addNode(second);
        assertEquals(second.getContent(), list.searchNode(second).getContent()); //Existing tail
        assertEquals(first.getContent(), list.searchNode(first).getContent()); //Existing head
        assertNull(list.searchNode(third)); //Non-existent node
        assertNull(list.searchNode((BaoNode<String>) null)); //null node
    }

    @Test
    void subListTests()
    {
        assertNull(list.subList(0)); //Empty list

        list.addNode(first);

        assertEquals(first.getContent(), list.subList(0).getNode(0).getContent()); //Single node

        list.addNode(second);
        list.addNode(third);
        BaoList <String> unchanged=list;

        BaoList <String> tmp=list.subList(2); //Full list copy
        assertEquals(first.getContent(), tmp.getNode(0).getContent());
        assertEquals(second.getContent(), tmp.getNode(1).getContent());
        assertEquals(third.getContent(), tmp.getNode(2).getContent());

        assertEquals(first.getContent(), list.subList(0).getNode(0).getContent()); //First node

        assertNull(list.subList(3)); //Too large
        assertNull(list.subList(-1)); //Too small

        assertEquals(first.getContent(), unchanged.getNode(0).getContent()); //Should be unchanged
        assertEquals(second.getContent(), unchanged.getNode(1).getContent());
        assertEquals(third.getContent(), unchanged.getNode(2).getContent());
    }

    @Test
    void clearTests()
    {
        list.clear(); //Empty list
        assertEquals(0, list.getSize());
        assertNull(list.getNode(0));

        list.addNode(first);
        list.addNode(second);
        list.addNode(third);
        list.clear(); //Populated list
        assertEquals(0, list.getSize());
        assertNull(list.getNode(0));
    }

    @Test
    void integrationTests()
    {
        list.addNode(first);
        list.addNode(second);
        list.addNode(third);

        list.removeNode(second);
        list.addNode(second);
        assertEquals(second.getContent(), list.getNode(2).getContent());
        list.clear();

        list.addNode(third);
        list.addNode(second);
        list.addNode(first);
        list.removeNode(third);
        assertEquals(first.getContent(), list.getNode(1).getContent());
    }
}
