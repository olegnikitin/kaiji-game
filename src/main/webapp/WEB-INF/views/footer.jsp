<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="footer.aboutus" var="footerAboutUs"/>
<spring:message code="aboutus.goalTitle" var="footerGoalTitle"/>
<spring:message code="aboutus.goal" var="footerGoal"/>
<spring:message code="aboutus.whoWeAreTitle" var="footerWhoWeAreTitle"/>
<spring:message code="aboutus.whoWeAre" var="footerWhoWeAre"/>
<spring:message code="aboutus.thanksTitle" var="footerThanksTitle"/>
<spring:message code="aboutus.close" var="footerClose"/>

<div class="span12">
    <button class="btn btn-link" data-toggle="modal" data-target="#about">
        ${footerAboutUs}
    </button>

    <div class="modal fade" id="about" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="background-image: url(http://s14.postimg.org/tv2nbq8sh/kaiji_05_op_rock_paper_scissors_cards.jpg)">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">${footerClose}</span></button>
                </div>
                <div class="modal-body" align="left">
                    <b>${footerGoalTitle}</b><br>
                    ${footerGoal}<br><br>
                    <b>${footerWhoWeAreTitle}</b><br>
                    ${footerWhoWeAre}<br><br>
                    <b>${footerThanksTitle}</b><br>
                    Evgeniy Paziy
                    Vladyslav Shelest
                    Ievgen Sukhov
                    Bohdan Shaposhnik
                    Boiko Eduard
                    Eugene Semenkov
                    Konstantin Shevchuk
                    Oleg Nikitin
                    Alexander Vorobyov
                    Kyrylo Bardachov
                    Oleksandra Sydorenko
                </div>
                <div class="modal-footer" style="background-image: url(http://s14.postimg.org/tv2nbq8sh/kaiji_05_op_rock_paper_scissors_cards.jpg)">
                    <button type="button" class="btn btn-default" data-dismiss="modal">${footerClose}</button>
                </div>
            </div>
        </div>
    </div>
</div>
