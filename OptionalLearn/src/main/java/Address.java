import java.util.Optional;

/**
 * @author someexp
 * @date 2021/6/2
 */
public class Address {

    private Country country;

    public Optional<Country> getCountry() {
        return Optional.ofNullable(country);
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
