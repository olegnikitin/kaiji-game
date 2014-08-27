<jsp:param name="socketActive" value="false"/>
<jsp:include page="chat_addons.jsp" />
<div class="container chat-wrapper">
    <form id="do-chat">
        <h2 class="alert alert-success"></h2>

        <h3> Online users : <label id="activeUsers" class="users"></label></h3>
        <table id="response" class="table table-bordered"></table>
        <fieldset>
            <legend>Enter your message...</legend>
            <div class="controls">
                <input type="text" class="input-block-level" placeholder="Print your message..." id="message"
                       style="height:60px"/>
                <input type="submit" class="btn btn-large btn-block btn-primary"
                       value="Send message"/>
            </div>
        </fieldset>
    </form>
</div>