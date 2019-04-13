<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<meta name="i18nName" content="#moduleName#">

<div class="app-inner-layout">

    <div class="app-inner-layout__header bg-gradient-light">
        <div class="app-page-title">
            <div class="page-title-wrapper">
                <div class="page-title-heading">
                    <div class="page-title-icon bg-happy-fisher">
                        <i class="#moduleIcon# text-white"></i>
                    </div>
                    <div>
                        <span data-i18n-txt="txt.#moduleName#.title"></span>
                        <div class="page-title-subheading"></div>
                    </div>
                </div>
                <div class="page-title-actions">
                    <button class="btn-icon btn-icon-only btn btn-link" data-toggle="collapse" href="#filterBar"
                            aria-expanded="false">
                        <i class="lnr-magnifier btn-icon-wrapper"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div id="filterBar" class="main-card card mb-sm-1 collapse">
        <div class="card-body">
            <form id="filterForm" action="" method="post" onsubmit="return false;">
                <div class="form-inline">
                    #filterContent#
                </div>
                <%--<div class="divider"></div>--%>
                <div class="form-inline float-right">
                    <button class="mb-2 mr-2 btn-icon btn btn-light" onclick="$.curd.reset();"
                            data-i18n-txt="txt.operate.reset">
                    </button>
                    <button class="mb-2 mr-2 btn-icon btn btn-primary" onclick="$.curd.retrieve();"
                            data-i18n-txt="txt.operate.retrieve">
                    </button>
                </div>
            </form>
        </div>
    </div>

    <div class="main-card card">
        <div class="card-body">
            <table id="tableObj" style="width:100%;" class="table table-hover table-striped">
            </table>
            <div id="toolbar">
                <button id="btnAdd" class="mb-2 mr-2 btn-icon btn btn-focus" style="display:none;">
                    <i class="lnr-plus-circle btn-icon-wrapper"></i>
                    <span data-i18n-txt="txt.operate.add"></span>
                </button>
                <button id="btnEdit" class="mb-2 mr-2 btn-icon btn btn-focus" style="display:none;">
                    <i class="lnr-menu-circle btn-icon-wrapper"></i>
                    <span data-i18n-txt="txt.operate.edit"></span>
                </button>
                <button id="btnRemove" class="mb-2 mr-2 btn-icon btn btn-focus" style="display:none;">
                    <i class="lnr-circle-minus btn-icon-wrapper"></i>
                    <span data-i18n-txt="txt.operate.remove"></span>
                </button>
            </div>
        </div>
    </div>

</div>

<%@ include file="../../common/scritps.jsp" %>
#datepickerJS#
<script type="text/javascript" src="./assets/scripts/views/view.#moduleName#.js"></script>
<script type="text/javascript">
    $(function () {
        initPagePermission();
        initTable();
    });
</script>
