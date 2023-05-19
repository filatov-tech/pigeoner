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
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            parentId: 1,
            sections: []
        }
    }

    componentDidMount() {
        if (this.props.sections) {
            this.setState({
                isLoaded: true,
                parentId: this.props.parentId,
                sections: this.props.sections
            });
        } else {
            this.initComponent();
        }
    }

    initComponent() {
        fetch('http://localhost:8080/api/v1/sections/')
            .then(resp => resp.json())
            .then(
                result => {
                    this.setState(() => {
                        return {
                            isLoaded: true,
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

    render() {
        const {error, isLoaded, sections, parentId} = this.state;
        if (error) {
            return <div>Ошибка! {error.message}</div>;
        } else if (!isLoaded) {
            return (
                <div className="spinner-border text-primary m-5" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
            );
        } else {
            const dataBsTarget = `#accordion-${parentId} .item-`;
            const ariaControls = `accordion-${parentId} .item-`;
            return (
                <div className="accordion" id={"accordion-" + parentId} role="tablist">
                    {sections.map(section => (
                            <div className="accordion-item" key={section.id}>
                                <h2 className="accordion-header" role="tab">
                                    <button className="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                            data-bs-target={dataBsTarget + section.id} aria-expanded="false"
                                            aria-controls={ariaControls + section.id}>
                                        {section.name}: {section.pigeonsNumber} гол.
                                    </button>
                                </h2>
                                <div className={"accordion-collapse collapse item-" + section.id} role="tabpanel">
                                    <div className="accordion-body">
                                        {(section.children.length > 0 && section.children[0].sectionType === "NEST")
                                            ?
                                            <div className="row g-2">
                                                {section.children.map(nest => (
                                                    <MyCard pigeons={nest.pigeons} name={nest.name}/>
                                                ))}
                                            </div>
                                            :
                                            (<React.Fragment>
                                                <strong>Секции:</strong>
                                                <MyAccordion sections={section.children} parentId={section.id}/>
                                            </React.Fragment>)}
                                        {section.pigeons.length > 0 &&
                                            <React.Fragment>
                                                <br/>
                                                <br/>
                                                <strong>Голуби:</strong>
                                                <PigeonList pigeons={section.pigeons} />
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
    render() {
        const pigeons = this.props.pigeons;
        return (<React.Fragment>
            {pigeons && pigeons.map(pigeon => (
                <p>{pigeon.ringNumber} {pigeon.isMale ? "M" : "Ж"}</p>
            ))}
        </React.Fragment>)
    }
}

class MyCard extends React.Component {
    render() {
        const pigeons = this.props.pigeons;
        const name = this.props.name;
        return (
            <div className="col-md-3 nest">
                <div className="card">
                    <div className="card-header">
                        <h5 className="mb-0">{name}</h5>
                    </div>
                    <div className="card-body">
                        <div className="row">
                            <MyLabel pigeon={pigeons[0]} />
                            <MyLabel pigeon={pigeons[1]} />
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

class MyLabel extends React.Component {
    render() {
        const pigeon = this.props.pigeon;
        return (
            <div className={"col-6 text-center"}>
                {pigeon
                    ? <div className={pigeon.male ? "label label-male" : "label label-female"}>{pigeon.ringNumber}</div>
                    : <div className={"label label-vacant"}>Cвободно</div>}
            </div>
        );
    }
}

const root = ReactDOM.createRoot(document.getElementById('pigeoner-accordion'));
root.render(<Main/>);