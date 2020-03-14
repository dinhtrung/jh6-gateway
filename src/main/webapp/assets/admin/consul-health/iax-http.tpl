<ul class="list-unstyled">
  <li><strong>Status:</strong> <span class="float-right badge badge-${ (status === 'UP' ? 'success' : 'danger blinking') }">${ status }</span></li>

  <% if (details.kong) { %>
  <li><strong>Kong:</strong> <span class="float-right badge badge-${ (details.kong.status === 'UP' ? 'success' : 'danger blinking')}">${ details.kong.status }</span>
    <% if (details.kong.details.error) { %>
      <p class="alert alert-error"><% details.kong.details.error %></p>
    <% } %>
  </li>
  <% } %>

  <% if (details.diskSpace) { %>
  <li><strong>Disk Space:</strong> <span class="float-right badge badge-${ (details.diskSpace.status === 'UP' ? 'success' : 'danger blinking')}">${ details.diskSpace.status }</span>
    <ul>
      <li><strong>Total:</strong> <code class="float-right">${ (details.diskSpace.details.total / 1073741824).toFixed(2) + 'GB' }</code></li>
      <li><strong>Free:</strong> <code class="float-right">${ (details.diskSpace.details.free/ 1073741824).toFixed(2) + 'GB' }</code></li>
      <li><strong>Threshold:</strong> <code class="float-right">${ (details.diskSpace.details.threshold/ 1048576).toFixed(2) + 'MB' }</code></li>
    </ul>
  </li>
  <% } %>

  <% if (details.hazelcast) { %>
    <li><strong>Hazelcast:</strong> <span class="float-right badge badge-${ (details.hazelcast.status === 'UP' ? 'success' : 'danger blinking')}">${ details.hazelcast.status }</span>
      <ul>
        <li><strong>Name:</strong> <code class="float-right">${ details.hazelcast.details.name }</code></li>
        <li><strong>UUID:</strong> <code class="float-right">${ details.hazelcast.details.uuid }</code></li>
      </ul>
    </li>
  <% } %>

  <% if (details.mongo) { %>
    <li><strong>mongo:</strong> <span class="float-right badge badge-${ (details.mongo.status === 'UP' ? 'success' : 'danger blinking')}">${ details.mongo.status }</span>
      <ul>
        <li><strong>Version:</strong> <code class="float-right">${ details.mongo.details.version }</code></li>
      </ul>
    </li>
  <% } %>

</ul>
