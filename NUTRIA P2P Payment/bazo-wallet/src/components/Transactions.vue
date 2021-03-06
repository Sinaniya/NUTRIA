<template>
  <div class="inbound">
    <div class="compact">
      <h1 class="display-4">{{ this.Translation.t('transactions.title') }}
      </h1>
      <hr>
      <div class="pos-rel user-funds-content">
        <spinner :is-loading="isLoading"></spinner>
        <!-- <label v-if="configured">{{Translation.t('transactions.description')}}</label> -->
        <div class="table-wrapper" v-if="!isLoading && !loadingError">
          <div v-if="configured"
          class="table-responsive">

          <b-table responsive small striped hover :items="this.tableRows" :fields="this.fields" :current-page="currentPage" :per-page="perPage">
            <template slot="address" scope="item">
              <div class="no-wrap">
                <span class="mono" v-bind:title="item.item.value">{{ formatBazoAddress(item.item.address) }}</span>&nbsp;
                <b-popover triggers="hover" :content="item.item.address" class="popover-element">
                  <i class="fa fa-eye increase-focus"></i>
                </b-popover>
              </div>
            </template>
			
			<template slot="hash" scope="item">
              <div class="no-wrap">
                <span class="mono" v-bind:title="item.item.value">{{ item.item.hash }}</span>&nbsp;
                <b-popover triggers="hover" :content="item.item.hash" class="popover-element">
                  <i class="fa fa-eye increase-focus"></i>
                </b-popover>
              </div>
            </template>

            <template slot="sender" scope="item">
              <div class="no-wrap">
                <span class="mono" v-bind:title="item.item.value">{{ formatBazoAddress(item.item.sender) }}</span>&nbsp;
                <b-popover triggers="hover" :content="item.item.sender" class="popover-element">
                  <i class="fa fa-eye increase-focus"></i>
                </b-popover>
              </div>
            </template>
            <template slot="amount" scope="item">
              <div class="no-wrap">
                <span class="mono" v-bind:title="item.item.value">{{ item.item.amount }}</span>&nbsp;
              </div>
            </template>
            <template slot="qr" scope="item">
              <div>
                <b-popover triggers="hover" :content="Translation.t('userAccounts.explorer')" class="popover-element">
                  <a :href="'http://explorer.oysy.io/account/' + item.item.sender">
                    <i class="fa fa-link" aria-hidden="true"></i>
                  </a>
                </b-popover>
              </div>
            </template>
            <template slot="actions" scope="item">
              <div>
                <div>
                  <b-button variant="danger" size="sm" @click.prevent="deleteAccount(item.item)">
                    <i class="fa fa-trash" aria-hidden="true"></i>
                  </b-button>
                  <b-popover triggers="hover" :content="Translation.t('userAccounts.deleteDescription')" class="popover-element">
                    <i class="fa fa-info-circle increase-focus"></i>
                  </b-popover>
                </div>
              </div>
            </template>
          </b-table>
        </div>
        <div class="" v-else>
          <b-alert v-html="Translation.t('userAccounts.notConfigured')" show variant="info"></b-alert>
        </div>
        <div class="reload-page" v-if="configured">
          <span class="btn btn-secondary oysy-button" @click.prevent="getRecentTransactions(false)">
            <i class="fa fa-refresh"></i>
            {{ this.Translation.t('userAccounts.reload') }}
          </span>
        </div>
        <hr v-if="configured">
      </div>
    </div>
  </div>
</div>
</template>

<script>
import Spinner from '@/components/Spinner';
import URIScheme from '@/services/URIScheme';
import Translation from '@/config/Translation';
import HttpService from '@/services/HttpService';
import elliptic from 'elliptic';

export default {
	name: 'transactions',
	data: function () {
		return {
			isLoading: true,
			loadingError: false,
			currentPage: 1,
			perPage: 15,
      Translation: Translation,
      recentTransactions: [],
      // eslint-disable-next-line
      curve: new elliptic.ec('p256')
		}
	},
	components: {
		Spinner
  },
	computed: {
    fields () {
      return {
        hash: {
          label: this.Translation.t('transactions.fields.hash'),
          sortable: false
        },
        amount: {
          label: this.Translation.t('transactions.fields.amount'),
          sortable: true
        },
        address: {
          label: this.Translation.t('transactions.fields.address'),
          sortable: true
        },
        sender: {
          label: this.Translation.t('transactions.fields.sender'),
          sortable: true
        }
      }
    },
    tableRows () {
      return this.recentTransactions;
    },
    allAccounts () {
      let accounts = this.$store.getters.bazoAccounts;
      return accounts;
    },
	allTransactions () {
      let transactions = this.$store.getters.bazoTransactions;
      return transactions;
    },
    configured () {
      return this.$store.getters.accountConfigured;
    },
    customURLUsed: function () {
      if (this.usingCustomHost) {
        return this.$store.getters.customURL;
      } return null;
    }
	},
  methods: {
    encodeBazoAddress (bazoAddress) {
      return URIScheme.encode(bazoAddress);
    },
    formatBazoAddress (address) {
      console.log('addr', address);
      if (address && address.length > 10) {
        return `${address.slice(0, 5)}..${address.slice(-5)}`;
      } return ''
    },
    verifyTransaction (hash, signature) {
      var key = this.curve.keyFromPublic({
        x: '8e487ed9332a0efe6470aec8f07bd031e49e9a392eb005eb4985abe99fc9beba',
        y: 'c846e45eca6c9e34c01a72e6f1b0f393170ae7abe451d9c1af300e734993454f'
      }, 'hex');
      return key.verify(hash, {r: signature.slice(0, 64), s: signature.slice(64, 128)})
    },
    getRecentTransactions () {
	    this.recentTransactions = this.$store.getters.bazoTransactions.reverse();
		//this.recentTransactions = this.recentTransactions.reverse();
	}
	
  },
	mounted: function () {
    window.transactions = this;
    window.elliptic = elliptic;
    this.isLoading = false;
    this.getRecentTransactions()
  }
};
</script>

<style lang="scss" scoped>
@import '../styles/variables';

h1 small {
	margin-top: 6px;
	font-size: 70%;
	font-weight: 300;
}
@media (max-width: 650px) {
	h1 small {
		margin-top: 15px;
		display: block;
		float: none;
	}
}
@media (max-width: 1050px) {
	.table-wrapper {
		overflow-x: auto;
		overflow-y: visible;
		width: 100%;
		max-width: 100%;

		@include light-scrollbar();


	}
}

.popover-element {
	color: #999;
	display: inline-block;
	vertical-align: middle;
	margin-left: 3px;
	cursor: help;
}

.create-new-address-button {
	.popover-element {
		margin-top: 3px;
		margin-left: 5px;
	}
}

.table /deep/ thead th {
	border-top: 0;
}

/* default is added to uninteresting rows */
.table /deep/ .table-default td {
	padding: 0.3rem 0.75rem;
	opacity: 0.4;
	font-style: italic;
}
.no-action-possible {
	font-style: italic;
	font-size: 90%;
}

.reload-page {
	// margin-top: 20px;
	text-align: center;
}
</style>
