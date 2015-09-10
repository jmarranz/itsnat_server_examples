package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;

/**
 *
 * @author jmarranz
 */
public class IntroductionTab extends TabBase
{
    public IntroductionTab(ExtJSExampleDocument parent)
    {
        super(parent);
    }

    public String getTitle()
    {
        return "Introduction";
    }

    public String getFragmentName()
    {
        return "extjsexample_introduction";
    }
}
