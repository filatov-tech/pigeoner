class Main extends React.Component {
    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col">
                        <MyAccordion />
                    </div>
                </div>
            </div>
        );
    }
}

class MyAccordion extends React.Component {

    static defaultProps = {
        baseUrl: `http://localhost:8080/api/v1/sections/info/`
    };

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            sections: [],
            pigeons: [],
            isChildrenLoaded: false
        }
        this.handleClick = this.handleClick.bind(this);
    }

    componentDidMount() {
        if (this.props.childSections) {
            this.resolveNeedForPigeons(this.props.parent)
            this.setState(() => {
                return {
                    isLoaded: true,
                    parent: this.props.parent,
                    sections: this.props.childSections
                }
            });
        } else {
            this.initComponent();
        }
    }

    initComponent() {
        fetch(MyAccordion.defaultProps.baseUrl)
            .then(resp => resp.json())
            .then(
                result => {
                    this.setState(() => {
                        return {
                            isLoaded: true,
                            parent: JSON.parse('{"id":1}'),
                            sections: result
                        }
                    });
                },
                error => {
                    this.setState({
                        isLoaded: true,
                        error
                    })
                }
            );
    }

    handleClick() {
        if (this.state.isChildrenLoaded) {
            return;
        }
        let parentsWhoNeedsChildren = JSON.parse(JSON.stringify(this.state.sections));
        let childrenRequests = parentsWhoNeedsChildren.map(section => fetch(MyAccordion.defaultProps.baseUrl + `${section.id}`));
        Promise.all(childrenRequests)
            .then(responses => Promise.all(responses.map(resp => resp.json())))
            .then(childSections => {
                for(let i = 0; i < childSections.length; i++) {
                    parentsWhoNeedsChildren[i].childs = childSections[i];
                    this.resolveNeedForPigeons(parentsWhoNeedsChildren[i]);
                }
                this.setState({
                    isChildrenLoaded: true,
                    sections: parentsWhoNeedsChildren
                });
            })
    }

    resolveNeedForPigeons(section) {
        const totalPigeonsNumber = section.pigeonsNumber;
        let pigeonsInChildren = 0;
        let sectionChilds = section.childs;
        for (const section of sectionChilds) {
            pigeonsInChildren += section.pigeonsNumber;
        }

        section.isNeedForPigeons = totalPigeonsNumber - pigeonsInChildren > 0;
        console.log("section.isNeedForPigeons");
        console.log(section.isNeedForPigeons);
    }

    render() {
        const {error, isLoaded, sections, parent} = this.state;
        if (error) {
            return <div>Ошибка! {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>
        } else {
            const dataBsTarget = `#accordion-${parent.id} .item-`;
            const ariaControls = `accordion-${parent.id} .item-`;
            return (
                <div className="accordion" id={"accordion-" + parent.id} role="tablist">
                    {sections.map(section => (
                        <div className="accordion-item" key={section.id}>
                            <h2 className="accordion-header" role="tab" onClick={this.handleClick}>
                                <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                        data-bs-target={dataBsTarget + section.id} aria-expanded="false"
                                        aria-controls={ariaControls + section.id}>
                                    {section.name}: {section.pigeonsNumber} гол.
                                </button>
                            </h2>
                            <div className={"accordion-collapse collapse item-" + section.id} role="tabpanel">
                                <div className="accordion-body">
                                    {!(section.sectionType === "NEST") && <strong>Секции:</strong>}
                                    {section.childs && <MyAccordion childSections={section.childs} parent={section}/>}
                                    <br/>
                                    <br/>
                                    {parent.isNeedForPigeons &&
                                        <React.Fragment>
                                            <strong>Голуби:</strong>
                                            <PigeonList sectionId={parent.id} />
                                        </React.Fragment>}
                                </div>
                            </div>
                        </div>
                        )
                    )}
                </div>
            );
        }
    }
}

class PigeonList extends React.Component {
    constructor(props) {
        super(props);
        this.state ={
            isLoaded: false,
            pigeons: []
        }
    }

    componentDidMount() {
        const locationId = this.props.sectionId;
        fetch(`http://localhost:8080/api/v1/sections/${locationId}/pigeons`)
            .then(resp => resp.json())
            .then(
                result => {
                    this.setState({
                        isLoaded: true,
                        pigeons: result
                    })
                },
                error => {
                    this.setState({
                        error
                    })
                }
            )
    }

    render() {
        const {error, isLoaded, pigeons} = this.state;
        console.log(pigeons);
        if (error) {
            return <div>Ошибка! {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>
        } else {
            return (<React.Fragment>
                {pigeons && pigeons.map(pigeon => (
                    <p>{pigeon.id} {pigeon.ringNumber}</p>
                ))}
            </React.Fragment>)
        }
    }

}

const root = ReactDOM.createRoot(document.getElementById('pigeoner-accordion'));
root.render(<Main/>);