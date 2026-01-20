<#import "layout/base.ftl" as layout>

<@layout.page title="Вход">

    <section class="bt-auth">
        <div class="bt-auth-card bt-card">
            <h1 class="bt-auth-title">С возвращением! 👑</h1>
            <p class="bt-auth-subtitle">
                Войди в свой аккаунт и продолжай планировать путешествия.
            </p>

            <#if error??>
                <div class="bt-alert bt-alert-error">
                    ${error?html}
                </div>
            </#if>

            <form method="post"
                  action="${request.contextPath}/login"
                  class="bt-form bt-form-auth">

                <div class="bt-form-group">
                    <label for="email" class="bt-label">E-mail</label>
                    <input type="email"
                           id="email"
                           name="email"
                           class="bt-input"
                           required
                           placeholder="barbie@example.com"
                           value="${email!""}">
                </div>

                <div class="bt-form-group">
                    <label for="password" class="bt-label">Пароль</label>
                    <input type="password"
                           id="password"
                           name="password"
                           class="bt-input"
                           required
                           placeholder="••••••••">
                </div>

                <div class="bt-form-actions bt-form-actions-column">
                    <button type="submit"
                            class="bt-btn bt-btn-primary bt-btn-block">
                        Войти
                    </button>

                    <p class="bt-auth-note">
                        Ещё нет аккаунта?
                        <a href="${request.contextPath}/register"
                           class="bt-link">
                            Зарегистрироваться
                        </a>
                    </p>
                </div>
            </form>
        </div>
    </section>

</@layout.page>
