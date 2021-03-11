package jp.co.tis.s2n.converterCommon.struts.analyzer;

import java.util.List;
import java.util.Map;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsAction;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm;

/**
 * Strutsの設定ファイルを構造化したクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StrutsAnalyzeResult {

    //StrutsActionのリスト
    private List<StrutsAction> actionList;
    //ActionFormのリスト
    private Map<String, StrutsForm> formList;
    private Map<String, Route> routes;
    private String module;

    public StrutsAnalyzeResult() {
        super();
    }

    /**
     * StrutsActionのリストを設定する。
     * @param actionList StrutsActionのリスト
     */
    public void setActionList(List<StrutsAction> actionList) {
        this.actionList = actionList;
    }

    /**
     * StrutsFormのリストを設定する。
     * @param formList StrutsFormのリスト
     */
    public void setFormList(Map<String, StrutsForm> formList) {
        this.formList = formList;
    }

    /**
     * route.xmlファイルのデータを設定する。
     * @param routes route.xmlファイルのデータ
     */
    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    /**
     * モジュールを設定する。
     * @param module モジュール
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * StrutsActionのクラスリストを取得する。
     * @return actionList StrutsActionのクラスリスト
     */
    public List<StrutsAction> getActionList() {
        return actionList;
    }

    /**
     * StrutsFormオブジェクトを保存するマップを取得する。
     * @return formList StrutsFormオブジェクトを保存するマップ
     */
    public Map<String, StrutsForm> getFormList() {
        return formList;
    }

    /**
     * route.xmlファイルのデータを取得する。
     * @return route.xmlファイルのデータ
     */
    public Map<String, Route> getRoutes() {
        return routes;
    }

    /**
     * モジュールを取得する。
     * @return モジュール
     */
    public String getModule() {
        return module;
    }

}
