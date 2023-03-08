package it.tndigitale.a4gutente.service.builder;

import it.tndigitale.a4gutente.dto.Distributore;
import it.tndigitale.a4gutente.repository.model.A4gtDistributore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

public class DistributoreBuilder {

    public static List<Distributore> from(Set<A4gtDistributore> a4gDistributori) {
        List<Distributore> distributori = new ArrayList<>();
        for (A4gtDistributore a4gDistributore : a4gDistributori) {
            Distributore distributoreOut = new Distributore();
            copyProperties(a4gDistributore, distributoreOut);
            distributori.add(distributoreOut);
        }
        return distributori;
    }

    public static List<A4gtDistributore> distributoreToA4gtDistributore(List<Distributore> distributori) {
        List<A4gtDistributore> a4gDistributori = new ArrayList<>();
        for (Distributore distributore : distributori) {
            A4gtDistributore distributoreOut = new A4gtDistributore();
            copyProperties(distributori, distributoreOut);
            a4gDistributori.add(distributoreOut);
        }
        return a4gDistributori;
    }
}
