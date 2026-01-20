<#import "../layout/base.ftl" as layout>
<@layout.page title="<#if tour??>Редактировать<#else>Новый</#if> тур">

    <section class="bt-section">
        <h1 class="bt-section-title"><#if tour??>Редактировать<#else>Создать новый</#if> тур</h1>

        <#if error??>
            <p class="bt-error" style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 20px;">
                ❌ ${error}
            </p>
        </#if>

        <form method="POST" class="bt-form" style="max-width: 600px;">
            <#if tour??>
                <input type="hidden" name="id" value="${tour.id}">
            </#if>

            <div class="bt-form-group">
                <label for="title" class="bt-label">Название тура *</label>
                <input type="text" id="title" name="title" class="bt-input" required
                       value="<#if tour??>${tour.title!""}</#if>">
            </div>

            <div class="bt-form-group">
                <label for="destination" class="bt-label">Направление *</label>
                <input type="text" id="destination" name="destination" class="bt-input" required
                       value="<#if tour??>${tour.destination!""}</#if>">
            </div>

            <div class="bt-form-group">
                <label for="price" class="bt-label">Цена (₽) *</label>
                <input type="number" id="price" name="price" class="bt-input" required step="0.01"
                       value="<#if tour??>${tour.price!""}</#if>">
            </div>

            <div class="bt-form-group">
                <label for="durationDays" class="bt-label">Длительность (дни) *</label>
                <input type="number" id="durationDays" name="durationDays" class="bt-input" required
                       value="<#if tour??>${tour.durationDays!""}</#if>">
            </div>

            <div class="bt-form-group">
                <label for="description" class="bt-label">Описание *</label>
                <textarea id="description" name="description" class="bt-input" required rows="5"><#if tour??>${tour.description!""}</#if></textarea>
            </div>

            <div class="bt-form-group">
                <label for="tags" class="bt-label">Теги (через запятую)</label>
                <input type="text" id="tags" name="tags" class="bt-input"
                       value="<#if tour??>${tour.tags!""}</#if>"
                       placeholder="пляж, пальмы, тропики">
            </div>

            <div class="bt-form-group">
                <label for="imageUrl" class="bt-label">URL картинки</label>
                <input type="text" id="imageUrl" name="imageUrl" class="bt-input"
                       value="<#if tour??>${tour.imageUrl!""}</#if>"
                       placeholder="paris.jpg">
                <small style="color: #666; display: block; margin-top: 8px;">
                    Файлы кладите в папку /static/img/tours/ (например: paris.jpg, tokyo.jpg)
                </small>
            </div>

            <div style="margin-top: 20px;">
                <button type="submit" class="bt-btn bt-btn-primary" style="margin-right: 10px;">
                    💾 Сохранить
                </button>
                <a href="${request.contextPath}/admin/tours" class="bt-btn bt-btn-secondary" style="text-decoration: none;">
                    ↩️ Назад
                </a>

                <#if tour??>
                    <button type="button" class="bt-btn bt-btn-danger" onclick="confirmDelete()" style="margin-left: 10px;">
                        🗑️ Удалить
                    </button>
                </#if>
            </div>
        </form>

        <#if tour??>
            <form method="POST" style="display: none;" id="deleteForm">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${tour.id}">
            </form>

            <script>
                function confirmDelete() {
                    if (confirm('Уверен(а)? Это действие нельзя отменить')) {
                        document.getElementById('deleteForm').submit();
                    }
                }
            </script>
        </#if>
    </section>

</@layout.page>
