package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

/**
 * struts-config.xml, validation.xmlから抽出したルート情報を構造化したクラス。<br>
 *
 * Nablarchのroutes.xmlを構成する１つのルート＝match１つになる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class Route {
    private String path;
    @SuppressWarnings("unused")
    private String forwardPath;
    private String controller;
    private String action;
    @SuppressWarnings("unused")
    private StrutsAction strutsAction;

    public Route(String path, String forwardPath, String controller, String action, StrutsAction strutsAction) {
        super();
        this.path = path;
        this.forwardPath = forwardPath;
        this.controller = controller;
        this.action = action;
        this.strutsAction = strutsAction;
    }

    /**
     * パスを取得する。
     * @return パス
     */
    public String getPath() {
        return path;
    }

    /**
     * controllerを取得する。
     * @return controller
     */
    public String getController() {
        return controller;
    }

    /**
     * アクションを取得する。
     * @return アクション
     */
    public String getAction() {
        return action;
    }

}
