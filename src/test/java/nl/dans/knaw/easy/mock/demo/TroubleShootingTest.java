package nl.dans.knaw.easy.mock.demo;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import nl.knaw.dans.common.lang.repo.DmoStoreId;
import nl.knaw.dans.easy.data.Data;
import nl.knaw.dans.easy.domain.model.AccessibleTo;
import nl.knaw.dans.easy.domain.model.FileItem;
import nl.knaw.dans.easy.domain.model.VisibleTo;
import nl.knaw.dans.easy.mock.BusinessMocker;
import nl.knaw.dans.easy.mock.FileMocker;

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Guide to interpret messages of exceptions thrown by {@link PowerMock}. The class provides some
 * examples of erroneous usage or unimplemented expectations of the the {@link BusinessMocker} library.
 */
@RunWith(PowerMockRunner.class)
public class TroubleShootingTest
{
    /**
     * The test methods using an instance explain possible causes of a problem.
     */
    static enum Message
    {
        NON_FIXED_COUNT_SET("last method called on mock already has a non-fixed count set."), //
        UNEXPECTED_METHOD("Unexpected method call"), //
        RECORD_STATE("calling verify is not allowed in record state"), //
        //
        ;
        private final String value;

        Message(final String value)
        {
            this.value = value;
        }

        public void assertEqual(final Throwable throwable)
        {
            assertThat(throwable.getMessage(), equalTo(value));
        }

        public void assertContains(final Throwable throwable)
        {
            assertThat(throwable.getMessage(), containsString(value));
        }
    }

    private BusinessMocker mock;

    @Before
    public void setUp() throws Exception
    {
        mock = new BusinessMocker();
    }

    /**
     * The actual {@link AbstractMocker} and property is just an example.
     */
    @Test
    public void duplicateExpectations() throws Exception
    {
        try
        {
            mock.file("test.txt").with(AccessibleTo.RESTRICTED_GROUP, VisibleTo.RESTRICTED_REQUEST);
            PowerMock.replayAll();
            PowerMock.verifyAll();
        }
        catch (final IllegalStateException e)
        {
            Message.NON_FIXED_COUNT_SET.assertEqual(e);
        }
    }

    /**
     * For the sake of the example {@link FileMocker#with(AccessibleTo)} is omitted. However, the mockers
     * are written on a n as-needed basis. So In practice you might have to write or extend a method for
     * some of the {@link AbstractMocker} implementations, or extend a constructor. Typically you need a
     * call to some variant of {@link EasyMock#expect(Object)} or {@link PowerMock}. Naming conventions:
     * <ul>
     * <li>plain "with" for non ambiguous signatures and
     * {@link IExpectationSetters#andStubReturn(Object)} which sets a default return value which is used
     * as a fallback only when regular .andReturn() have been used up</li>
     * <li>plain "withXxxx" to disambiguate a signatures and {@link IExpectationSetters#andStubReturn(Object)}</li>
     * <li>plain "expectYyyy" for a more specific number of times</li>
     * </ul>
     * 
     * @throws Exception
     */
    @Test
    public void missingExpectations() throws Exception
    {
        try
        {
            mock.file("test.txt");

            PowerMock.replayAll();

            final FileItem fileItem = (FileItem) Data.getEasyStore().retrieve(new DmoStoreId("easyfile:1"));
            fileItem.getAccessibleTo();
        }
        catch (final AssertionError e)
        {
            Message.UNEXPECTED_METHOD.assertContains(e);
        }
    }

    @Test
    public void replayMissing() throws Exception
    {
        try
        {
            mock.file("test.txt").with(AccessibleTo.RESTRICTED_REQUEST, VisibleTo.ANONYMOUS);
            // replay should belong here
            Data.getEasyStore().retrieve(new DmoStoreId("easyfile:1"));
            PowerMock.verifyAll();
        }
        catch (final IllegalStateException e)
        {
            Message.RECORD_STATE.assertEqual(e);
        }
    }
}
