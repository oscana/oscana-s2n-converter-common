package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import org.w3c.dom.Element;

import jp.co.tis.s2n.converterCommon.util.StringUtils;

/**
 * StrutsのActionForwardを構造化したクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ActionForward extends AbstractStrutsBean {

    private StrutsAction parentAction;

    public ActionForward(Element elm, StrutsAction parentAction) {
        this.basisElement = elm;
        this.parentAction = parentAction;
    }

    /**
     * 親のアクションを取得する。
     * @return 親のアクション
     */
    public StrutsAction getParentAction() {
        return parentAction;
    }

    /**
     * name属性の値を取得する。
     * @return name属性の値
     */
    public String getName() {
        return StringUtils.normalizeNull(basisElement.getAttribute("name"));
    }

    /**
     * パス属性の値を取得する。
     * @return パス属性の値
     */
    public String getPath() {
        return StringUtils.normalizeNull(basisElement.getAttribute("path"));
    }

    /**
     * これと同じXMLNodeへの参照を持つ別のクラスを作成する。
     * @param strutsAction 親ノード
     * @return ActionForwardオブジェクト
     */
    protected ActionForward copy(StrutsAction strutsAction) {

        return new ActionForward(this.basisElement, strutsAction);
    }

}
