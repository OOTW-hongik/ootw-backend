package OOTWhongik.OOTW.domain.member.dto.request;


import OOTWhongik.OOTW.domain.member.domain.Location;

public class LocationRequest {
    private String location;

    public Location getLocation() {
        return Location.findByValue(location);
    }
}
