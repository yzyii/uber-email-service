<template>
  <div id="app">
    <div class="header">
      <label>Email Service</label>
    </div>
    <div class="container">
      <div class="addressSection">
        <label class="leftPad">To</label>
        <multiaddressinput class="rightPad" addressee-type="to"/>
      </div>
      <div class="addressSection">
        <label class="leftPad">Cc</label>
        <multiaddressinput class="rightPad" addressee-type="cc"/>
      </div>
      <div class="addressSection">
        <label class="leftPad">Bcc</label>
        <multiaddressinput class="rightPad" addressee-type="bcc"/>
      </div>
      <div class="addressSection">
        <label class="leftPad">From</label>
        <input class="rightPad" v-model="from"/>
      </div>
      <div class="addressSection">
        <input v-model="subject" placeholder="Subject"/>
      </div>
      <div class="contentWrapper">
        <textarea class="content" v-model="content"/>
      </div>
    </div>
    <button @click="sendEmail">Send</button>
  </div>
</template>

<script>
import MultiAddressInput from './MultiAddressInput.vue'
export default {
  components: { multiaddressinput: MultiAddressInput },
  name: 'app',
  data () {
    return {
      from: '',
      subject: '',
      content: '',
    }
  },
  methods: {
    sendEmail() {
      if (this.$store.tos.length === 0) {
        alert("The \"To\" field cannot be empty. Please specify at least one recipient.");
        return;
      }

      if (isEmpty(this.from, "From")) { return; }

      if (!isValidEmails(this.$store.tos, "To")) { return; }
      if (!isValidEmails(this.$store.ccs, "Cc")) { return; }
      if (!isValidEmails(this.$store.bccs, "Bcc")) { return; }
      if (!isValidEmail(this.from, "From")) { return; }

      if (isEmpty(this.subject, "Subject")) { return; }
      if (isEmpty(this.content, "Contents")) { return; }

      fetch('./api/email', {
        method: "post",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          tos: this.$store.tos,
          ccs: this.$store.ccs,
          bccs: this.$store.bccs,
          from: this.from,
          subject: this.subject,
          content: this.content
        })
      })
      .then(function(response) {
        if (response.status !== 200) {
          alert(response);
        } else {
          alert("Your email was sent successfully!");
          location.reload();
        }
      })
      .catch(function(err) {
        console.log('Fetch Error :-S', err);
      });
    }
  }
}

function isEmpty(value, field) {
  if (!value || !value.trim()) {
    alert("The \"" + field + "\" field cannot be empty.");
    return true;
  }
  return false;
}

function isValidEmails(emails, field) {
  for (var i = 0; i < emails.length; i++) {
    if (!isValidEmail(emails[i], field)) {
      return false;
    }
  }
  return true;
}

function isValidEmail(email, field) {
  var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  var result =  emailRegex.test(email) ? true : false;
  if (!result) {
    alert("The address \"" + email + "\" in the \"" + field + "\" field is invalid. Please make sure that all addresses are properly formed.");
  }
  return result;
}
</script>

/* Global CSS */
<style>
body {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
input {
  font-family: inherit;
  font-size: 1rem;
  border: 0;
  outline: none;
  width: 100%;
  color: #2c3e50;
}
textarea {
  border: 0;
  padding: 0.3rem;
  outline: none;
  font-family: inherit;
  font-size: 1rem;
  color: #2c3e50;
}
</style>

/* Local CSS */
<style scoped>
.header, .container {
  width: 50rem;
  margin: 0 auto;
  border: 1px solid #404040;
}
.header {
  background-color: #404040;
  color: #fff;
  padding: 1rem 0rem;
  font-size: 1.5rem;
  font-weight: 600;
}
.container {
  text-align: left;
  margin-bottom: 1rem;
}
.addressSection {
  border-bottom: 1px solid #404040;
  height: 1.5rem;
  padding: 0.3rem;
}
.leftPad {
  float: left;
  width: 3rem;
  color: #777;
}
.rightPad {
  display: inline-block;
  width: calc(100% - 3rem);
}
.contentWrapper, .content {
  height: 50vh;
}
.content {
  max-height: 50vh;
  width: 100%;
  max-width: 100%;
  overflow-y: scroll;
  resize: none;
  box-sizing: border-box;
}
button {
  background-color: #337ab7;
  border: 1px solid #2e6da4;
  border-radius: 5px;
  color: white;
  padding: 0.5rem 2rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1rem;
  transition-duration: 0.2s;
}
button:hover {
  background-color: #286090;
  border-color: #204d74;
}
button:active {
  background-color: #204d74;
  border-color: #122b40;
}
</style>
