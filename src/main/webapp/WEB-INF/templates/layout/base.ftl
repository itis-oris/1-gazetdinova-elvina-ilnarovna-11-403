<#macro page title>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>${title?html} — travel planner</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet"
              href="${request.contextPath}/static/css/style.css">
    </head>
    <body class="bt-body">
    <div class="bt-page">

        <!-- Хедер -->
        <header class="bt-header">
            <div class="bt-container bt-header-inner">
                <div class="bt-logo">
                    <a href="${request.contextPath}/" class="bt-logo-link">
                        travel <span>planner</span>
                    </a>
                </div>

                <nav class="bt-nav">
                    <a href="${request.contextPath}/tours"
                       class="bt-nav-link">
                        Туры
                    </a>

                    <#if user??>
                        <a href="${request.contextPath}/profile"
                           class="bt-nav-link">
                            Профиль
                        </a>

                        <#if user.role?? && user.role?string == "ADMIN">
                            <a href="${request.contextPath}/admin/tours"
                               class="bt-nav-link">
                                Админка
                            </a>
                        </#if>

                        <a href="${request.contextPath}/logout"
                           class="bt-nav-link bt-nav-link-accent">
                            Выйти
                        </a>
                    <#else>
                        <a href="${request.contextPath}/login"
                           class="bt-nav-link bt-nav-link-accent">
                            Войти
                        </a>
                    </#if>
                </nav>
            </div>
        </header>

        <main class="bt-main">
            <div class="bt-container">
                <#nested>
            </div>
        </main>

        <!-- Футер -->
        <footer class="bt-footer">
            <div class="bt-container bt-footer-inner">
            <span class="bt-footer-text">
                travel planner • zero thoughts, full schedule ✨
            </span>
            </div>
        </footer>
    </div>

    <script src="${request.contextPath}/static/js/main.js"></script>
    </body>
    </html>
</#macro>
