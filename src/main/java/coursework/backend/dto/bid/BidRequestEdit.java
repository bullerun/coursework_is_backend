package coursework.backend.dto.bid;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequestEdit {
    @NotBlank(message = "Bid name can not be blank")
    @Size(max = 255, message = "Bid name's length can not be greater than 255")
    private String name;

    @NotBlank(message = "Bid description can not be blank")
    @Size(max = 1000, message = "Bid description's length can not be greater than 1000")
    private String description;

    @Min(value = 0, message = "Bid cost can not be negative")
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

    @NotNull(message = "Expiration time is required")
    private LocalDateTime expiredAt;
}
