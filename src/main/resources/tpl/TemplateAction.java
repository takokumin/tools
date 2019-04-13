package cn.tgm.msf.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.tgm.msf.bean.#beanName#Bean;
import cn.tgm.msf.common.Constants;
import cn.tgm.msf.common.Utils;
import cn.tgm.msf.service.CommonService;

public class #actionName#Action extends CURDAction<#beanName#Bean> {

    private static final long serialVersionUID = -1L;

    protected static final Logger logger = LogManager.getLogger(#actionName#Action.class.getName());

    protected CommonService service = new CommonService();

    protected #beanName#Bean bean = new #beanName#Bean();

    @Override
    protected Logger getLogger() {

        return logger;
    }

    @Override
    protected String getCountSQL() {

        return "get#beanName#CountSQL";
    }

    @Override
    protected String getListSQL() {

        return "get#beanName#ListSQL";
    }

    @Override
    protected String getAddSQL() {

        return "insert#beanName#SQL";
    }

    @Override
    protected String getUpdateSQL() {

        return "update#beanName#SQL";
    }

    @Override
    protected String getRemoveSQL() {

        return "delete#beanName#SQL";
    }

    @Override
    protected String getBatchRemoveSQL() {

        return "delete#beanName#BatchSQL";
    }

    @Override
    protected String getObjectSQL() {

        return "get#beanName#SQL";
    }

    @Override
    protected String[] getLockKeys() {

        return new String[]{bean.#getPkMethod#()};
    }

    @Override
    protected boolean beforeAdd() {

        bean.#setPkMethod#(Utils.UUID32());
        bean.setDel_flag(Constants.NO);

        return true;
    }

    @Override
    public #beanName#Bean getModel() {

        return bean;
    }
}
