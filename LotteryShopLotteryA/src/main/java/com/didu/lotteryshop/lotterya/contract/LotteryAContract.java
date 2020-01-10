package com.didu.lotteryshop.lotterya.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class LotteryAContract extends Contract {
    private static final String BINARY = "0x608060405260008060006101000a81548160ff02191690831515021790555034801561002a57600080fd5b5060405160408061202e8339810180604052604081101561004a57600080fd5b81019080805190602001909291908051906020019092919050505033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555030600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050611eb48061017a6000396000f3fe6080604052600436106100f35760003560e01c8063addd240b1161008a578063cae60eb811610059578063cae60eb8146105f4578063d1ffdc5914610684578063d8da2cab146106af578063ea94a1d714610706576100f3565b8063addd240b146102fb578063afad8e0c146103c6578063b4d322a91461048e578063b7f03422146105b9576100f3565b806341c0e1b5116100c657806341c0e1b5146101d8578063478c4e0e146101ef5780634ae0cb4c146102065780638d6cc56d146102c0576100f3565b80630cda84b3146100f85780631b5a29c61461010257806338cff0431461012d578063398b98c8146101c1575b600080fd5b610100610747565b005b34801561010e57600080fd5b506101176108a1565b6040518082815260200191505060405180910390f35b34801561013957600080fd5b5061016c6004803603602081101561015057600080fd5b81019080803563ffffffff169060200190929190505050610905565b604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018263ffffffff1663ffffffff1681526020019250505060405180910390f35b3480156101cd57600080fd5b506101d6610a00565b005b3480156101e457600080fd5b506101ed610a76565b005b3480156101fb57600080fd5b50610204610ae9565b005b34801561021257600080fd5b506102456004803603602081101561022957600080fd5b81019080803563ffffffff169060200190929190505050610b87565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561028557808201518184015260208101905061026a565b50505050905090810190601f1680156102b25780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102cc57600080fd5b506102f9600480360360208110156102e357600080fd5b8101908080359060200190929190505050610ccb565b005b6103c46004803603604081101561031157600080fd5b810190808035906020019064010000000081111561032e57600080fd5b82018360208201111561034057600080fd5b8035906020019184600183028401116401000000008311171561036257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803563ffffffff169060200190929190505050610d2f565b005b3480156103d257600080fd5b5061048c600480360360208110156103e957600080fd5b810190808035906020019064010000000081111561040657600080fd5b82018360208201111561041857600080fd5b8035906020019184600183028401116401000000008311171561043a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506110c8565b005b34801561049a57600080fd5b50610564600480360360408110156104b157600080fd5b81019080803590602001906401000000008111156104ce57600080fd5b8201836020820111156104e057600080fd5b8035906020019184600183028401116401000000008311171561050257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803563ffffffff16906020019092919050505061119a565b604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018263ffffffff1663ffffffff1681526020019250505060405180910390f35b3480156105c557600080fd5b506105f2600480360360208110156105dc57600080fd5b810190808035906020019092919050505061139a565b005b34801561060057600080fd5b5061060961158d565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561064957808201518184015260208101905061062e565b50505050905090810190601f1680156106765780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561069057600080fd5b50610699611689565b6040518082815260200191505060405180910390f35b3480156106bb57600080fd5b506106c4611724565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561071257600080fd5b506107456004803603602081101561072957600080fd5b81019080803563ffffffff1690602001909291905050506117a8565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806107f05750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b806108485750600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61085157600080fd5b3373ffffffffffffffffffffffffffffffffffffffff167f414dedf228aec26de6f6661c8d4a63c3ec9e2d8b595a47495d4c7cfc1f14bc94346040518082815260200191505060405180910390a2565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146108fd57600080fd5b600654905090565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461096257600080fd5b8263ffffffff1660098054905011156109f05760098363ffffffff168154811061098857fe5b9060005260206000200160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098463ffffffff16815481106109c957fe5b9060005260206000200160000160149054906101000a900463ffffffff16915091506109fb565b600080809050915091505b915091565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610a5a57600080fd5b60008060006101000a81548160ff021916908315150217905550565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610ad057600080fd5b3373ffffffffffffffffffffffffffffffffffffffff16ff5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610b4357600080fd5b60016000610b519190611baa565b60096000610b5f9190611bcb565b6040518060200160405280600081525060079080519060200190610b84929190611bec565b50565b6060600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610be357600080fd5b8163ffffffff166001805490501115610cb35760018263ffffffff1681548110610c0957fe5b906000526020600020018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ca75780601f10610c7c57610100808354040283529160200191610ca7565b820191906000526020600020905b815481529060010190602001808311610c8a57829003601f168201915b50505050509050610cc6565b6040518060200160405280600081525090505b919050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610d2557600080fd5b8060068190555050565b60008163ffffffff16118015610d4c575060648163ffffffff1611155b610d5557600080fd5b60608290506003815114610d6857600080fd5b6000809054906101000a900460ff1615610d8157600080fd5b610d9c8263ffffffff16600654611b7b90919063ffffffff16565b3414610da757600080fd5b60016000806101000a81548160ff0219169083151502179055506000600a60643481610dcf57fe5b04029050600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610e3b573d6000803e3d6000fd5b506000601e60643481610e4a57fe5b04029050600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610eb6573d6000803e3d6000fd5b5060006008866040518082805190602001908083835b60208310610eef5780518252602082019150602081019050602083039250610ecc565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090508060405180604001604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018763ffffffff1681525090806001815401808255809150509060018203906000526020600020016000909192909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548163ffffffff021916908363ffffffff160217905550505050806008876040518082805190602001908083835b602083106110255780518252602082019150602081019050602083039250611002565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908054611066929190611c6c565b5060018690806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906110a4929190611bec565b505060008060006101000a81548160ff021916908315150217905550505050505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461112257600080fd5b6008816040518082805190602001908083835b602083106111585780518252602082019150602081019050602083039250611135565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060006111979190611bcb565b50565b600080600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146111f757600080fd5b60606008856040518082805190602001908083835b6020831061122f578051825260208201915060208101905060208303925061120c565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020805480602002602001604051908101604052809291908181526020016000905b8282101561132c578382906000526020600020016040518060400160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900463ffffffff1663ffffffff1663ffffffff168152505081526020019060010190611282565b5050505090508363ffffffff168151111561138757808463ffffffff168151811061135357fe5b602002602001015160000151818563ffffffff168151811061137157fe5b6020026020010151602001519250925050611393565b60008080905092509250505b9250929050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146113f457600080fd5b6000811161140157600080fd5b6040518060400160405280600381526020017f31323300000000000000000000000000000000000000000000000000000000008152506007908051906020019061144c929190611bec565b506008600760405180828054600181600116156101000203166002900480156114ac5780601f1061148a5761010080835404028352918201916114ac565b820191906000526020600020905b815481529060010190602001808311611498575b5050915050908152602001604051809103902060099080546114cf929190611c6c565b507f35f7834c68eed1868256d1d51e03a7ab0ea1675884d2dc59b1e0057d671b08066007604051808060200182810382528381815460018160011615610100020316600290048152602001915080546001816001161561010002031660029004801561157c5780601f106115515761010080835404028352916020019161157c565b820191906000526020600020905b81548152906001019060200180831161155f57829003601f168201915b50509250505060405180910390a150565b6060600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146115e957600080fd5b60078054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561167f5780601f106116545761010080835404028352916020019161167f565b820191906000526020600020905b81548152906001019060200180831161166257829003601f168201915b5050505050905090565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146116e557600080fd5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631905090565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461178057600080fd5b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461180257600080fd5b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16311161184857600080fd5b60006009805490501161185a57600080fd5b6000603260646103e8600654028161186e57fe5b040290506000601490506000838202905060008163ffffffff1690506009805490508263ffffffff16106118a55760098054905090505b600083830390505b818163ffffffff1610156119ce576118c3611d5c565b60098263ffffffff16815481106118d657fe5b906000526020600020016040518060400160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900463ffffffff1663ffffffff1663ffffffff16815250509050806000015173ffffffffffffffffffffffffffffffffffffffff166108fc826020015163ffffffff1688029081150290604051600060405180830381858888f193505050501580156119bf573d6000803e3d6000fd5b505080806001019150506118ad565b506009805490508263ffffffff161015611a3b577fd65d258b234711af408e3e020fbba4839378c73d2152a1b371507d3c34d47d8860018660405180831515151581526020018263ffffffff1663ffffffff1681526020019250505060405180910390a150505050611b78565b7fd65d258b234711af408e3e020fbba4839378c73d2152a1b371507d3c34d47d8860008660405180831515151581526020018263ffffffff1663ffffffff1681526020019250505060405180910390a16000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163111611ad157600080fd5b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f19350505050158015611b72573d6000803e3d6000fd5b50505050505b50565b60008082840290506000841480611b9a575082848281611b9757fe5b04145b611ba057fe5b8091505092915050565b5080546000825590600052602060002090810190611bc89190611d92565b50565b5080546000825590600052602060002090810190611be99190611dbe565b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611c2d57805160ff1916838001178555611c5b565b82800160010185558215611c5b579182015b82811115611c5a578251825591602001919060010190611c3f565b5b509050611c689190611e1b565b5090565b828054828255906000526020600020908101928215611d4b5760005260206000209182015b82811115611d4a5782826000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000820160149054906101000a900463ffffffff168160000160146101000a81548163ffffffff021916908363ffffffff160217905550505091600101919060010190611c91565b5b509050611d589190611dbe565b5090565b6040518060400160405280600073ffffffffffffffffffffffffffffffffffffffff168152602001600063ffffffff1681525090565b611dbb91905b80821115611db75760008181611dae9190611e40565b50600101611d98565b5090565b90565b611e1891905b80821115611e1457600080820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556000820160146101000a81549063ffffffff021916905550600101611dc4565b5090565b90565b611e3d91905b80821115611e39576000816000905550600101611e21565b5090565b90565b50805460018160011615610100020316600290046000825580601f10611e665750611e85565b601f016020900490600052602060002090810190611e849190611e1b565b5b5056fea165627a7a72305820645740149931c2d9924be848e874282648106c73fcb52c42acb4bccc587c30790029";

    public static final String FUNC_UPDATEBUYLOTTERYLOCKBOOL = "updateBuyLotteryLockBool";

    public static final String FUNC_UPDATEPRICE = "updatePrice";

    public static final String FUNC_SHOWPRICE = "ShowPrice";

    public static final String FUNC_SHOWCONTRACTADR = "ShowContractAdr";

    public static final String FUNC_SHOWCONTRACTBALANCE = "ShowContractBalance";

    public static final String FUNC_SHOWLUCKNUM = "ShowLuckNum";

    public static final String FUNC_GETBUYERLUCKLIST = "getBuyerLuckList";

    public static final String FUNC_GETBUYLUCKNUMBERARRAY = "getBuyLuckNumberArray";

    public static final String FUNC_GETBUYMAPPING = "getBuyMapping";

    public static final String FUNC_CONTRACTBALANCEIN = "ContractBalanceIn";

    public static final String FUNC_DRAWLOTTERY = "DrawLottery";

    public static final String FUNC_PAYBONUS = "PayBonus";

    public static final String FUNC_BUYLOTTERY = "BuyLottery";

    public static final String FUNC_RESETBUYMAPPING = "resetBuyMapping";

    public static final String FUNC_RESETDATA = "resetData";

    public static final String FUNC_KILL = "kill";

    public static final Event CONTRACTBALANCEINEVENT_EVENT = new Event("ContractBalanceInEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAYBONUSISEXECUTEEVENT_EVENT = new Event("PayBonusIsExecuteEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint32>() {}));
    ;

    public static final Event DRAWLOTTERYEVENT_EVENT = new Event("DrawLotteryEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected LotteryAContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LotteryAContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LotteryAContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LotteryAContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ContractBalanceInEventEventResponse> getContractBalanceInEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRACTBALANCEINEVENT_EVENT, transactionReceipt);
        ArrayList<ContractBalanceInEventEventResponse> responses = new ArrayList<ContractBalanceInEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ContractBalanceInEventEventResponse typedResponse = new ContractBalanceInEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContractBalanceInEventEventResponse> contractBalanceInEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ContractBalanceInEventEventResponse>() {
            @Override
            public ContractBalanceInEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRACTBALANCEINEVENT_EVENT, log);
                ContractBalanceInEventEventResponse typedResponse = new ContractBalanceInEventEventResponse();
                typedResponse.log = log;
                typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContractBalanceInEventEventResponse> contractBalanceInEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTBALANCEINEVENT_EVENT));
        return contractBalanceInEventEventFlowable(filter);
    }

    public List<PayBonusIsExecuteEventEventResponse> getPayBonusIsExecuteEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAYBONUSISEXECUTEEVENT_EVENT, transactionReceipt);
        ArrayList<PayBonusIsExecuteEventEventResponse> responses = new ArrayList<PayBonusIsExecuteEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PayBonusIsExecuteEventEventResponse typedResponse = new PayBonusIsExecuteEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.IsExecute = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.cycleIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PayBonusIsExecuteEventEventResponse> payBonusIsExecuteEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PayBonusIsExecuteEventEventResponse>() {
            @Override
            public PayBonusIsExecuteEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAYBONUSISEXECUTEEVENT_EVENT, log);
                PayBonusIsExecuteEventEventResponse typedResponse = new PayBonusIsExecuteEventEventResponse();
                typedResponse.log = log;
                typedResponse.IsExecute = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.cycleIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PayBonusIsExecuteEventEventResponse> payBonusIsExecuteEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYBONUSISEXECUTEEVENT_EVENT));
        return payBonusIsExecuteEventEventFlowable(filter);
    }

    public List<DrawLotteryEventEventResponse> getDrawLotteryEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DRAWLOTTERYEVENT_EVENT, transactionReceipt);
        ArrayList<DrawLotteryEventEventResponse> responses = new ArrayList<DrawLotteryEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DrawLotteryEventEventResponse typedResponse = new DrawLotteryEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.currentLuckNum = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DrawLotteryEventEventResponse> drawLotteryEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DrawLotteryEventEventResponse>() {
            @Override
            public DrawLotteryEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DRAWLOTTERYEVENT_EVENT, log);
                DrawLotteryEventEventResponse typedResponse = new DrawLotteryEventEventResponse();
                typedResponse.log = log;
                typedResponse.currentLuckNum = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DrawLotteryEventEventResponse> drawLotteryEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DRAWLOTTERYEVENT_EVENT));
        return drawLotteryEventEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> updateBuyLotteryLockBool() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEBUYLOTTERYLOCKBOOL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updatePrice(BigInteger lotteryPrice) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEPRICE, 
                Arrays.<Type>asList(new Uint256(lotteryPrice)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> ShowPrice() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> ShowContractAdr() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWCONTRACTADR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> ShowContractBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWCONTRACTBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> ShowLuckNum() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWLUCKNUM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> getBuyerLuckList(BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBUYERLUCKLIST, 
                Arrays.<Type>asList(new Uint32(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint32>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> getBuyLuckNumberArray(BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBUYLUCKNUMBERARRAY, 
                Arrays.<Type>asList(new Uint32(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> getBuyMapping(String buyMappingKey, BigInteger index) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBUYMAPPING, 
                Arrays.<Type>asList(new Utf8String(buyMappingKey),
                new Uint32(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint32>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> ContractBalanceIn(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONTRACTBALANCEIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> DrawLottery(BigInteger randomNumber) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DRAWLOTTERY, 
                Arrays.<Type>asList(new Uint256(randomNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> PayBonus(BigInteger cycleIndex) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYBONUS, 
                Arrays.<Type>asList(new Uint32(cycleIndex)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> BuyLottery(String luckyNumber, BigInteger multipleNumber, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUYLOTTERY, 
                Arrays.<Type>asList(new Utf8String(luckyNumber),
                new Uint32(multipleNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> resetBuyMapping(String buyMappingKey) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESETBUYMAPPING, 
                Arrays.<Type>asList(new Utf8String(buyMappingKey)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> resetData() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESETDATA, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> kill() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_KILL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LotteryAContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryAContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LotteryAContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryAContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LotteryAContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LotteryAContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LotteryAContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LotteryAContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String adjustFundAddress, String bonusPoolAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(adjustFundAddress),
                new Address(bonusPoolAddress)));
        return deployRemoteCall(LotteryAContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String adjustFundAddress, String bonusPoolAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(adjustFundAddress),
                new Address(bonusPoolAddress)));
        return deployRemoteCall(LotteryAContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String adjustFundAddress, String bonusPoolAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(adjustFundAddress),
                new Address(bonusPoolAddress)));
        return deployRemoteCall(LotteryAContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String adjustFundAddress, String bonusPoolAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(adjustFundAddress),
                new Address(bonusPoolAddress)));
        return deployRemoteCall(LotteryAContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ContractBalanceInEventEventResponse extends BaseEventResponse {
        public String buyer;

        public BigInteger money;
    }

    public static class PayBonusIsExecuteEventEventResponse extends BaseEventResponse {
        public Boolean IsExecute;

        public BigInteger cycleIndex;
    }

    public static class DrawLotteryEventEventResponse extends BaseEventResponse {
        public String currentLuckNum;
    }
}
