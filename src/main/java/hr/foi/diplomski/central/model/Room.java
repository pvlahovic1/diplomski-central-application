package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(generator = "room_id_seq")
    @Column(name = "id_room")
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name = "id_room")
    @JsonManagedReference
    private List<Sensor> sensors = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}
