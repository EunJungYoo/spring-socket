
'use strict';

var vm = new Vue({
  el: '#app',
  data: {
    room_name : '',
    chatrooms: [
    ]
  },
  created() {
    this.findAllRoom();
  },
  methods: {
    findAllRoom: function() {
      axios.get('/chat/rooms').then(response => { this.chatrooms = response.data; });
    },
    createRoom: function() {
      if("" === this.room_name) {
        alert("방 제목을 입력해 주십시요.");
        return;
      } else {
        var params = new URLSearchParams();
        params.append("name",this.room_name);
        axios.post('/chat/room', params)
          .then(
            response => {
              alert(response.data.roomName+"방 개설에 성공하였습니다.")
              this.room_name = '';
              this.findAllRoom();
            }
          )
          .catch( response => { alert("채팅방 개설에 실패하였습니다."); } );
      }
    },
    enterRoom: function(roomId) {
      var sender = prompt('대화명을 입력해 주세요.');
      if(sender !== "") {
        localStorage.setItem('wschat.sender',sender);
        localStorage.setItem('wschat.roomId',roomId);
        location.href="/chat/room/enter/"+roomId;
      }
    }
  }
});