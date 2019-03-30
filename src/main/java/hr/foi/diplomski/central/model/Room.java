package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(generator = "room_id_seq")
    @Column(name = "id_room")
    private Integer id;

    @Column(name = "room_name")
    private String roomName;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Sensor> sensors = new ArrayList<>();

}
