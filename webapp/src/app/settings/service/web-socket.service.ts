import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import * as Stomp from 'stompjs';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private webSocket: any;
  roomSubject = new Subject<string>();
  sensorSubject = new Subject<number>();

  constructor() {
    this.initWebSocket();
  }

  private initWebSocket() {
    let socket = new WebSocket(environment.webSocketUrl);
    this.webSocket = Stomp.over(socket);
    let that = this;
    this.webSocket.connect({}, function (frame) {
      that.webSocket.subscribe("/topic/room", function (message) {
        that.roomSubject.next(message.body);
      });
      that.webSocket.subscribe("/topic/sensor", function (message) {
        that.sensorSubject.next(message.body);
      });
    }, function (error) {
      console.log("Greska u web socketu " + error);
      setTimeout(() => {
        console.log("Pokusavam se ponovo spojiti na web socket");
        that.initWebSocket();
      }, 10000);
    });
  }

}
