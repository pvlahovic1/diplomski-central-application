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
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_width")
    private Double width;

    @Column(name = "room_length")
    private Double length;

    @Column(name = "room_height")
    private Double height;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_room")
    @JsonManagedReference
    private List<Sensor> sensors = new ArrayList<>();

    public Double calculateMaxDistance() {
        return Math.sqrt(Math.pow(width, 2) + Math.pow(length, 2) + Math.pow(height, 2));
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", width='" + width + '\'' +
                ", length='" + length + '\'' +
                '}';
    }
}
