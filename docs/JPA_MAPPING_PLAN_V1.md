# Maisto pristatymo sistema – JPA Mapping Plan v1

## Statusas

`M02 BASELINE v1`

Šis planas susieja Java modelį su patvirtinta `DB Schema v1`. SQL schema yra
duomenų bazės struktūros šaltinis, todėl produkcinė Hibernate konfigūracija
naudoja `hibernate.hbm2ddl.auto=validate` ir DB struktūros nekeičia.

## Entity ir lentelės

| Entity | Lentelė | ID | Svarbiausi ryšiai |
|---|---|---|---|
| `User` | `users` | `IDENTITY` | `SINGLE_TABLE`, discriminator `user_type` |
| `Admin` | `users` | paveldimas | `@DiscriminatorValue("ADMIN")` |
| `Client` | `users` | paveldimas | `delivery_address`, `bonus_points` |
| `Driver` | `users` | paveldimas | `vehicle_type` |
| `RestaurantOwner` | `users` | paveldimas | `@DiscriminatorValue("RESTAURANT_OWNER")` |
| `Restaurant` | `restaurants` | `IDENTITY` | `owner_id → users.id` (`ManyToOne`) |
| `MenuItem` | `menu_items` | `IDENTITY` | `restaurant_id → restaurants.id` (`ManyToOne`) |
| `FoodOrder` | `food_orders` | `IDENTITY` | klientas, restoranas, pasirenkamas vairuotojas |
| `OrderItem` | `order_items` | `IDENTITY` | užsakymas ir meniu elementas |
| `OrderMessage` | `order_messages` | `IDENTITY` | užsakymas ir siuntėjas |
| `Review` | `reviews` | `IDENTITY` | užsakymas, autorius ir vienas vertinimo target |

## Bendros taisyklės

- Visi enum saugomi kaip tekstas su `@Enumerated(EnumType.STRING)`.
- Visi privalomi foreign key ryšiai žymimi `optional=false` ir `nullable=false`.
- `ManyToOne` ryšiai kraunami su `FetchType.LAZY`.
- `FoodOrder → OrderItem` naudoja `cascade=ALL` ir `orphanRemoval=true`.
- Laiko laukai pildomi Java pusėje per konstruktorių ir `@PrePersist`.
- `username` bei `email` yra unikalūs, `email` yra privalomas.
- BCrypt reikšmei naudojamas `password_hash VARCHAR(60)`.
- Produkcinė DB sukuriama vykdant patvirtintą `V1__create_food_delivery_schema.sql`;
  Hibernate tik patikrina schemos atitikimą.

## Pirmas CRUD vertical slice

```text
RestaurantOwner → Restaurant → MenuItem → MySQL
```

Pjūvį sudaro `UserRepository`, `RestaurantRepository`, `MenuItemRepository` ir
atitinkami service sluoksnio metodai. H2 integracinis testas patikrina, kad šie
trys entity gali būti išsaugoti ir jų ryšiai lieka teisingi.

## Dar likę M02 darbai

- Paleisti patvirtintą SQL schemą vietiniame MySQL.
- Užpildyti vietinį `db.properties` pagal `db.properties.example`.
- Paleisti `StartHibernate` ir gauti sėkmingą `SELECT 1` bei schema validation.
- Paleisti `mvn test` ir patvirtinti H2 integracinį testą.
- Paleisti `MysqlCrudSmoke` ir patvirtinti realų MySQL CRUD ciklą.
