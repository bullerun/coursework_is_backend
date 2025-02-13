package coursework.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TenderRequestDTO {

    @NotBlank(message = "Tender name can not be blank")
    private String name;

    @NotBlank(message = "Tender description can not be blank")
    private String description;

    @NotNull(message = "Tender cost is required")
    @DecimalMin(value = "0.0", message = "Tender cost can not be negative")
    private Long cost;

    @NotBlank(message = "Region can not be blank")
    @Pattern(regexp = """
            Республика\\sАдыгея |
             Республика\\sАлтай |
             Республика\\sБашкортостан |
             Республика\\sБурятия |
             Республика\\sДагестан |
             Республика\\sИнгушетия |
             Кабардино-Балкарская\\sРеспублика |
             Республика\\sКалмыкия |
             Карачаево-Черкесская\\sРеспублика |
             Республика\\sКарелия |
             Республика\\sКоми |
             Республика\\sМарий\\sЭл |
             Республика\\sМордовия |
             Республика\\sСаха\\s\\(Якутия\\) |
             Республика\\sСеверная\\sОсетия-Алания |
             Республика\\sТатарстан |
             Республика\\sТыва |
             Удмуртская\\sРеспублика |
             Республика\\sХакасия |
             Чеченская\\sРеспублика |
             Чувашская\\sРеспублика |
             Алтайский\\sкрай |
             Забайкальский\\sкрай |
             Камчатский\\sкрай |
             Краснодарский\\sкрай |
             Красноярский\\sкрай |
             Пермский\\sкрай |
             Приморский\\sкрай |
             Ставропольский\\sкрай |
             Хабаровский\\sкрай |
             Амурская\\sобласть |
             Архангельская\\sобласть |
             Астраханская\\sобласть |
             Белгородская\\sобласть |
             Брянская\\sобласть |
             Владимирская\\sобласть |
             Волгоградская\\sобласть |
             Вологодская\\sобласть |
             Воронежская\\sобласть |
             Ивановская\\sобласть |
             Иркутская\\sобласть |
             Калининградская\\sобласть |
             Калужская\\sобласть |
             Кемеровская\\sобласть |
             Кировская\\sобласть |
             Костромская\\sобласть |
             Курганская\\sобласть |
             Курская\\sобласть |
             Ленинградская\\sобласть |
             Липецкая\\sобласть |
             Магаданская\\sобласть |
             Московская\\sобласть |
             Мурманская\\sобласть |
             Нижегородская\\sобласть |
             Новгородская\\sобласть |
             Новосибирская\\sобласть |
             Омская\\sобласть |
            Оренбургская\\sобласть |
            Орловская\\sобласть |
            Пензенская\\sобласть |
            Псковская\\sобласть |
            Ростовская\\sобласть |
            Рязанская\\sобласть |
            Самарская\\sобласть |
            Саратовская\\sобласть|
             Сахалинская\\sобласть|
             Свердловская\\sобласть|
             Смоленская\\sобласть|
             Тамбовская\\sобласть |
             Тверская\\sобласть |
             Томская\\sобласть |
             Тульская\\sобласть |
             Тюменская\\sобласть |
             Ульяновская\\sобласть |
             Челябинская\\sобласть |
             Ярославская\\sобласть |
             Москва |
             Санкт-Петербург |
             Еврейская\\sАО |
             Ненецкий\\sАО |
             Ханты-Мансийский\\sАО |
             Чукотский\\sАО |
             Ямало-Ненецкий\\sАО""", flags = Pattern.Flag.COMMENTS, message = "Invalid region name")
    private String region;

    @NotNull(message = "Organization ID is required")
    private UUID organizationId;

    @NotNull(message = "Expiration time is required")
    private LocalDateTime expiredAt;
}
