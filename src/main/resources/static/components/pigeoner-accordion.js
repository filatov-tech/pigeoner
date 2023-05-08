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
            this.setState(() => {
                return {
                    isLoaded: true,
                    parentId: this.props.parentId,
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
                            parentId: 1,
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
                }
                this.setState({
                    isChildrenLoaded: true,
                    sections: parentsWhoNeedsChildren
                });
            })
    }

    render() {
        const {error, isLoaded, sections, parentId} = this.state;
        if (error) {
            return <div>Ошибка! {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>
        } else {
            const dataBsTarget = `#accordion-${parentId} .item-`;
            const ariaControls = `accordion-${parentId} .item-`;
            return (
                <div className="accordion" id={"accordion-" + parentId} role="tablist">
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
                                    {section.childs && <MyAccordion childSections={section.childs} parentId={section.id}/>}
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

const root = ReactDOM.createRoot(document.getElementById('pigeoner-accordion'));
root.render(<Main/>);