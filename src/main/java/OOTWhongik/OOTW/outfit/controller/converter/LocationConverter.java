package OOTWhongik.OOTW.outfit.controller.converter;

import org.springframework.core.convert.converter.Converter;
import OOTWhongik.OOTW.member.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter implements Converter<String, Location> {
    @Override
    public Location convert(String source) {
        return Location.from(source);
    }
}
