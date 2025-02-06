CREATE TABLE IF NOT EXISTS Tender
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            TEXT   NOT NULL,
    description     TEXT   NOT NULL,
    cost            BIGINT NOT NULL,
    region          TEXT   NOT NULL CHECK (region IN ('Республика Адыгея',
                                                      'Республика Алтай',
                                                      'Республика Башкортостан',
                                                      'Республика Бурятия',
                                                      'Республика Дагестан',
                                                      'Республика Ингушетия',
                                                      'Кабардино-Балкарская Республика',
                                                      'Республика Калмыкия',
                                                      'Карачаево-Черкесская Республика',
                                                      'Республика Карелия',
                                                      'Республика Коми',
                                                      'Республика Марий Эл',
                                                      'Республика Мордовия',
                                                      'Республика Саха (Якутия)',
                                                      'Республика Северная Осетия-Алания',
                                                      'Республика Татарстан',
                                                      'Республика Тыва',
                                                      'Удмуртская Республика',
                                                      'Республика Хакасия',
                                                      'Чеченская Республика',
                                                      'Чувашская Республика',
                                                      'Алтайский край',
                                                      'Забайкальский край',
                                                      'Камчатский край',
                                                      'Краснодарский край',
                                                      'Красноярский край',
                                                      'Пермский край',
                                                      'Приморский край',
                                                      'Ставропольский край',
                                                      'Хабаровский край',
                                                      'Амурская область',
                                                      'Архангельская область',
                                                      'Астраханская область',
                                                      'Белгородская область',
                                                      'Брянская область',
                                                      'Владимирская область',
                                                      'Волгоградская область',
                                                      'Вологодская область',
                                                      'Воронежская область',
                                                      'Ивановская область',
                                                      'Иркутская область',
                                                      'Калининградская область',
                                                      'Калужская область',
                                                      'Кемеровская область',
                                                      'Кировская область',
                                                      'Костромская область',
                                                      'Курганская область',
                                                      'Курская область',
                                                      'Ленинградская область',
                                                      'Липецкая область',
                                                      'Магаданская область',
                                                      'Московская область',
                                                      'Мурманская область',
                                                      'Нижегородская область',
                                                      'Новгородская область',
                                                      'Новосибирская область',
                                                      'Омская область',
                                                      'Оренбургская область',
                                                      'Орловская область',
                                                      'Пензенская область',
                                                      'Псковская область',
                                                      'Ростовская область',
                                                      'Рязанская область',
                                                      'Самарская область',
                                                      'Саратовская область',
                                                      'Сахалинская область',
                                                      'Свердловская область',
                                                      'Смоленская область',
                                                      'Тамбовская область',
                                                      'Тверская область',
                                                      'Томская область',
                                                      'Тульская область',
                                                      'Тюменская область',
                                                      'Ульяновская область',
                                                      'Челябинская область',
                                                      'Ярославская область',
                                                      'Москва',
                                                      'Санкт-Петербург',
                                                      'Еврейская АО',
                                                      'Ненецкий АО',
                                                      'Ханты-Мансийский АО',
                                                      'Чукотский АО',
                                                      'Ямало-Ненецкий АО')),
    organization_id UUID   NOT NULL REFERENCES Organization (id),
    version         BIGINT           DEFAULT 1,
    tender_status   TEXT   NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    expired_at      TIMESTAMP,
    owner_id        UUID   NOT NULL REFERENCES users (id)
);