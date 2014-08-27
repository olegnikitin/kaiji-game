<script>
            var wsocketHeader;
            var wsocketSession;
            var serviceLocationHeader = "ws://" + document.location.host + "/chat/";
            var serviceLocationSession = "ws://" + document.location.host + "/session";
            var groupHeader = 'DP-059';

            function connectToChatServerHeader() {
                wsocketHeader = new WebSocket(serviceLocationHeader + groupHeader);
                wsocketHeader.onmessage = onMessageReceivedHeader;
            }

            function connectToSessionServerHeader() {
                wsocketSession = new WebSocket(serviceLocationSession);
            }

            function onMessageReceivedHeader(evt) {
                document.getElementById("notificationMessage").innerHTML = 'New messages'
                setInterval(blinker, 1000);
            }

            window.onbeforeunload = function (evt) {
                wsocketHeader.close();
                wsocketSession.close();
            }

            var element;
            $(document).ready(function () {
                connectToSessionServerHeader()
                setInterval(sessionActivity, 2000);
                element = document.getElementById("notificationMessage");
                element.innerHTML = ''
                if ('${param.socketActive}' != 'false') {
                    connectToChatServerHeader();
                }

                if (<%=ChatUtils.getUnReadMessages().get((String)pageContext.getAttribute("userName")).equals(true)%>) {
                    element.innerHTML = 'New message'
                    setInterval(blinker, 1000);
                }

            })

            var shown = true;

            function blinker() {
                if (shown) {
                    element.style.color = 'red'
                    shown = false;
                } else {
                    element.style.color = 'blue'
                    shown = true;
                }
            }

            function sessionActivity() {
                var message = '{"nickname":"${pageContext.getAttribute("userName")}"}';
                wsocketSession.send(message);
            }

        </script>