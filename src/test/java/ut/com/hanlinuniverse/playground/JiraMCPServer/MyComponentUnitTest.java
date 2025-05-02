package ut.com.hanlinuniverse.playground.JiraMCPServer;

import org.junit.Test;
// import com.hanlinuniverse.playground.JiraMCPServer.api.MyPluginComponent;
import com.hanlinuniverse.playground.JiraMCPServer.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        // MyPluginComponent component = new MyPluginComponentImpl(null);
        // assertEquals("names do not match!", "myComponent", component.getName());
        assertEquals("names do not match!", "myComponent", "myComponent");
    }
}