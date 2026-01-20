<#import "layout/base.ftl" as layout>
<@layout.page title="Регистрация">

    <section class="auth-section">
        <div class="auth-card">
            <h1 class="auth-title">Создай аккаунт</h1>
            <p class="auth-subtitle">
                Чтобы сохранять бронирования и быть main character в каждом отпуске ✨
            </p>

            <#if error??>
                <div class="bt-alert bt-alert-error">
                    ${error?html}
                </div>
            </#if>

            <form method="post" action="${request.contextPath}/register" class="bt-form">
                <div class="bt-form-group">
                    <label for="username" class="bt-label">Никнейм</label>
                    <input type="text" id="username" name="username"
                           class="bt-input" required
                           value="${username!""}">
                </div>

                <div class="bt-form-group">
                    <label for="email" class="bt-label">E-mail</label>
                    <input type="email" id="email" name="email"
                           class="bt-input" required
                           value="${email!""}">
                </div>

                <div class="bt-form-group">
                    <label for="password" class="bt-label">
                        Пароль <span class="bt-hint">(минимум 6 символов)</span>
                    </label>
                    <input type="password" id="password" name="password"
                           class="bt-input" required minlength="6">
                </div>

                <div class="bt-form-actions">
                    <button type="submit" class="bt-btn bt-btn-primary">
                        Зарегистрироваться
                    </button>
                </div>

                <p class="auth-footer">
                    Уже есть аккаунт?
                    <a href="${request.contextPath}/login" class="bt-link">
                        Войти
                    </a>
                </p>
            </form>
        </div>
    </section>

</@layout.page>
