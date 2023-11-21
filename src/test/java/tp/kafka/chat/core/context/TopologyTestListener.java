package tp.kafka.chat.core.context;

import org.apache.kafka.streams.TopologyTestDriver;
import org.springframework.test.annotation.DirtiesContext.HierarchyMode;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * closes the TopologyTestDriver and recreates the Kafka Context after each
 * test. This prevents tests from leaking kafka messages to other tests.
 */
public class TopologyTestListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        var driver = testContext.getApplicationContext().getBean(TopologyTestDriver.class);
        if (!driver.producedTopicNames().isEmpty()) {
            driver.close();
            testContext.markApplicationContextDirty(HierarchyMode.EXHAUSTIVE);
        }
    }
}
