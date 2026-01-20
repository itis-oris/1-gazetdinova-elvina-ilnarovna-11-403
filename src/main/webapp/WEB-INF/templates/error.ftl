<#import "layout/base.ftl" as layout>

<@layout.page title="Ой, ошибочка">

    <section class="bt-section bt-section-center">
        <div class="bt-error-card bt-card">
            <h1 class="bt-error-title">
                Ой... что-то пошло не по плану 💔
            </h1>

            <p class="bt-error-code">
                <#if status??>
                    Код ошибки: ${status}
                <#else>
                    Непредвиденная ошибка
                </#if>
            </p>

            <p class="bt-error-message">
                <#if message??>
                    ${message?html}
                <#else>
                    Скорее всего сервер решил взять небольшой перерыв.
                    Попробуй ещё раз чуть позже.
                </#if>
            </p>

            <div class="bt-error-actions">
                <a href="${request.contextPath}/"
                   class="bt-btn bt-btn-primary">
                    На главную
                </a>
                <a href="javascript:history.back()"
                   class="bt-btn bt-btn-ghost">
                    Вернуться назад
                </a>
            </div>
        </div>
    </section>

</@layout.page>
