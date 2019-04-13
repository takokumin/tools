<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<meta name="i18nName" content="#moduleName#">

<div class="app-inner-layout p-3">

    <div class="app-page-title app-page-title-simple">
        <div class="page-title-wrapper">
            <div class="page-title-heading">
                <div>
                    <div class="page-title-head center-elem">
                    <span class="d-inline-block pr-2">
                        <i class="#moduleIcon# opacity-6"></i>
                    </span>
                        <span class="d-inline-block" data-i18n-txt="txt.#moduleName#.edit.title"></span>
                    </div>
                    <div class="page-title-subheading opacity-10">
                        <nav class="" aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item">
                                    <a>
                                        <i aria-hidden="true" class="fa fa-home"></i>
                                    </a>
                                </li>
                                <li class="breadcrumb-item">
                                    <a href="#" onclick="loadParent();"
                                       data-i18n-txt="txt.#moduleName#.title"></a>
                                </li>
                                <li class="active breadcrumb-item" aria-current="page">
                                    <span data-i18n-txt="txt.#moduleName#.edit.title"></span>
                                </li>
                            </ol>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="main-card mb-3 card">
        <div class="card-header">
            <button class="mr-2 btn-icon btn btn-light btn-back">
                <i class="lnr-chevron-left-circle btn-icon-wrapper"></i>
                <span data-i18n-txt="txt.common.back"></span>
            </button>
            <button class="mr-2 btn-icon btn btn-primary btn-ok" type="submit">
                <i class="lnr-checkmark-circle btn-icon-wrapper"></i>
                <span data-i18n-txt="txt.common.ok"></span>
            </button>
        </div>
        <div class="card-body">
            <form id="#moduleName#Form" action="#ifPrefix#_edit" method="post">
                <input type="hidden" id="mode" name="mode" value="${param.mode}"/>
                <input type="hidden" id="#pkId#" name="#pkName#" value="${param.#pkName#}"/>
                <input type="hidden" id="updateOn" name="updateOn"/>
                #editContent#
            </form>
        </div>
        <div class="d-block text-right card-footer">
            <button class="mr-2 btn-icon btn btn-light btn-back">
                <i class="lnr-chevron-left-circle btn-icon-wrapper"></i>
                <span data-i18n-txt="txt.common.back"></span>
            </button>
            <button class="mr-2 btn-icon btn btn-primary btn-ok" type="submit">
                <i class="lnr-checkmark-circle btn-icon-wrapper"></i>
                <span data-i18n-txt="txt.common.ok"></span>
            </button>
        </div>
    </div>

</div>

<%@ include file="../../common/scritps.jsp" %>
#datepickerJS#
<script type="text/javascript" src="./assets/scripts/views/view.#moduleName#.js"></script>
<script type="text/javascript">
    $(function () {

        $('.btn-back').on('click', loadParent);

        $('.btn-ok').on('click', function () {
            $('##moduleName#Form').submit();
        });

        initEdit();

    });
</script>
