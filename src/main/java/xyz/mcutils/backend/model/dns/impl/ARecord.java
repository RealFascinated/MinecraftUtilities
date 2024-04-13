package xyz.mcutils.backend.model.dns.impl;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.mcutils.backend.model.dns.DNSRecord;

import java.net.InetAddress;

@Setter @Getter
@NoArgsConstructor
public final class ARecord extends DNSRecord {
    /**
     * The address of this record, null if unresolved.
     */
    private String address;

    public ARecord(@NonNull org.xbill.DNS.ARecord bootstrap) {
        super(Type.A, bootstrap.getTTL());
        InetAddress address = bootstrap.getAddress();
        this.address = address == null ? null : address.getHostAddress();
    }
}