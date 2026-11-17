package com.lms.learning_management_system.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


/**
 * Базовый интерфейс для MapStruct мапперов, обеспечивающий стандартные операции
 * преобразования между сущностями (Entity) и объектами передачи данных (DTO).
 *
 * @param <E> Тип сущности базы данных (Entity).
 * @param <D> Тип основного DTO для чтения/отображения (Read DTO).
 * @param <C> Тип DTO, используемого исключительно для создания новой сущности (Create DTO).
 * @param <U> Тип DTO, используемого для частичного или полного обновления существующей сущности (Update DTO).
 *
 * <h3>Пример использования:</h3>
 * <pre>{@code
 * @Mapper(componentModel = "spring")
 * public interface CourseMapper extends BaseMapper<Course, CourseDto, CourseCreateDto, CourseUpdateDto> {
 *     // кастомные маппинги
 * }
 * }</pre>
 */
public interface BaseMapper<E, D, C, U> {

    /**
     * Преобразует сущность базы данных в объект передачи данных (DTO).
     *
     * @param entity исходная сущность из базы данных
     * @return соответствующий DTO объект
     */
    D toDto(E entity);

    /**
     * Преобразует DTO создания (Create DTO) в новую сущность базы данных.
     * <p>
     *
     * @param createDto объект, содержащий данные для инициализации новой сущности
     * @return созданная сущность
     */
    E toEntity(C createDto);

    /**
     * Обновляет поля существующей сущности значениями из DTO (Update DTO).
     *
     * @param updateDto объект, содержащий новые значения для обновления
     * @param entity    сущность, которая будет изменена
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(U updateDto, @MappingTarget E entity);
}
