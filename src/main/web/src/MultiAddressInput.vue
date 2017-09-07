<template>
  <div id="multiaddressinput">
    <div class="container">
      <ul>
        <li v-for="(email, index) in emails">
          {{ email.text }}
          <button @click="deleteEmail(index)">x</button>
        </li>
      </ul>
      <div class="stretch">
        <input @blur="addEmail"
               @keydown.enter="addEmail"
               @keydown.tab="addEmail($event)"
               @keydown.188="addEmail($event)"
               @keydown.space="addEmail($event)"
               @keydown.delete="deleteEmail"
               v-model="inputField"/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'multiaddressinput',
  props: ['addresseeType'],
  data () {
    return {
      emails: [],
      inputField: ''
    }
  },
  methods: {
    addEmail(event) {
      if (this.inputField !== '') {
        if (event) { event.preventDefault(); }
        
        //TODO: Given more time, this should be changed to an input field so that it can be edited
        //      instead of having to be deleted and retyped in.
        this.emails.push({
          text: this.inputField
        })
        if (this.addresseeType === 'to') {
          this.$store.tos.push(this.inputField);
        } else if (this.addresseeType === 'cc') {
          this.$store.ccs.push(this.inputField);
        } else if (this.addresseeType === 'bcc') {
          this.$store.bccs.push(this.inputField);
        }

        this.inputField = '';
      } else if (event && event.code !== 'Tab') {
        event.preventDefault();
      }
    },
    deleteEmail(index) {
      // Set index to -1 to delete the last added entry if the index is not valid and the input field is empty.
      if (!(index && typeof index === 'number') && this.inputField === '') {
        index = -1;
      }

      if (index && typeof index === 'number') {
         this.emails.splice(index, 1);
        if (this.addresseeType === 'to') {
          this.$store.tos.splice(index, 1);
        } else if (this.addresseeType === 'cc') {
          this.$store.ccs.splice(index, 1);
        } else if (this.addresseeType === 'bcc') {
          this.$store.bccs.splice(index, 1);
        }
      }
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  width: 100%;
  height: 1.5rem;
}
.stretch {
  flex: 1; 
}
ul {
  display: inline-block;
  list-style-type: none;
  margin: 0;
  padding: 0;
}
li {
  display: inline-block;
  padding-right: 0.5rem;
}
button {
  background-color: #fff; 
  border: none;
  color: #999;
  padding: 0px 3px;
  text-align: center;
  cursor: pointer;
  font-weight: bold;
}
button:hover {
  color: #333;
}
</style>
